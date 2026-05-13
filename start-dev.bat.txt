@echo off
echo Starting Spring Boot Dev Server...
mvn spring-boot:run -Dspring-boot.run.profiles=dev
pause