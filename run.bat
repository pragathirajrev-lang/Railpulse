@echo off
set DIR=%~dp0
cd /d "%DIR%"

echo =========================================================================
echo   Starting Railway Big Data Spark Analytics and Demand Forecasting Platform
echo =========================================================================

call .\mvnw.cmd spring-boot:run

pause

