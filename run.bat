@echo off
set DIR=%~dp0
cd /d "%DIR%"

echo =========================================================================
echo   Starting Railway Big Data Spark Analytics and Demand Forecasting Platform
echo =========================================================================

set JVM_ARGS=--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.invoke=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.util.concurrent=ALL-UNNAMED --add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.base/sun.nio.cs=ALL-UNNAMED --add-opens=java.base/sun.security.action=ALL-UNNAMED --add-opens=java.base/sun.util.calendar=ALL-UNNAMED -Djdk.reflect.useDirectMethodHandle=false

if exist "target\railway-spark-analytics-1.0.0.jar" (
    echo Launching packaged JAR on Java 21...
    java %JVM_ARGS% -jar target\railway-spark-analytics-1.0.0.jar
) else (
    echo Building and starting with Maven wrapper...
    call mvnw.cmd spring-boot:run
)

pause

