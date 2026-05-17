@echo off
title UC18 - Spring Security JWT + OAuth2
echo ================================================
echo  UC18 - Spring Security JWT + Google OAuth2
echo  Port: 8085
echo ================================================
echo.

cd /d "%~dp0"

echo [1/2] Building UC18 (clean + compile, skip tests)...
call mvn clean package -DskipTests -q
if %ERRORLEVEL% neq 0 (
    echo.
    echo [FAILED] Maven build failed. Check output above.
    pause
    exit /b 1
)

echo [2/2] Starting UC18 on port 8085...
echo.
echo  Endpoints:
echo    POST http://localhost:8085/api/auth/register
echo    POST http://localhost:8085/api/auth/login
echo    GET  http://localhost:8085/api/auth/me
echo    GET  http://localhost:8085/api/auth/health
echo    GET  http://localhost:8085/oauth2/authorization/google
echo.
echo  Google OAuth2 callback (add to Google Cloud Console):
echo    http://localhost:8085/login/oauth2/code/google
echo.
call mvn spring-boot:run
