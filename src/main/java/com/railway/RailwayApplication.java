package com.railway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RailwayApplication {

    private static final Logger log = LoggerFactory.getLogger(RailwayApplication.class);

    public static void main(String[] args) {
        log.info("Starting Railway Big Data Spark Analytics & Demand Forecasting Platform...");
        SpringApplication.run(RailwayApplication.class, args);
        log.info("Railway Analytics Platform is running! Navigate to http://localhost:8080 in your browser.");
    }
}

