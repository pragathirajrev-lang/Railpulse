package com.railway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
public class DatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/railway_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}")
    private String mysqlUrl;

    @Value("${spring.datasource.username:root}")
    private String username;

    @Value("${spring.datasource.password:}")
    private String password;

    @Value("${spring.datasource.driver-class-name:com.mysql.cj.jdbc.Driver}")
    private String driverClassName;

    @Bean
    @Primary
    public DataSource dataSource() {
        log.info("Checking connection to MySQL database at {}...", mysqlUrl);
        boolean mysqlAvailable = false;
        try {
            Class.forName(driverClassName);
            try (Connection conn = DriverManager.getConnection(mysqlUrl, username, password)) {
                if (conn != null && !conn.isClosed()) {
                    mysqlAvailable = true;
                    log.info("Successfully connected to MySQL database!");
                }
            }
        } catch (Exception ex) {
            log.warn("MySQL database check failed: {}. Falling back to embedded H2 (MySQL Mode).", ex.getMessage());
        }

        if (mysqlAvailable) {
            return DataSourceBuilder.create()
                    .driverClassName(driverClassName)
                    .url(mysqlUrl)
                    .username(username)
                    .password(password)
                    .build();
        } else {
            log.info("Initializing fallback In-Memory H2 Database with MySQL emulation mode...");
            return DataSourceBuilder.create()
                    .driverClassName("org.h2.Driver")
                    .url("jdbc:h2:mem:railway_db;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE")
                    .username("sa")
                    .password("")
                    .build();
        }
    }
}

