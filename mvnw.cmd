@echo off
set DIR=%~dp0
set MVN_CMD=%DIR%tools\maven\apache-maven-3.9.16\bin\mvn.cmd
if exist "%MVN_CMD%" (
    call "%MVN_CMD%" %*
) else (
    call mvn %*
)

