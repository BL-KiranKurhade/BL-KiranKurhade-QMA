@echo off
setlocal EnableDelayedExpansion
title QMA - Start Dev Environment
color 0A

set ROOT=%~dp0

echo.
echo  ============================================================
echo    QMA - Quantity Measurement App  ^|  Dev Startup
echo  ============================================================
echo.
echo  Services will start in this order:
echo    1. Eureka Server     (port 8761)
echo    2. API Gateway       (port 8080)
echo    3. Auth Service      (port 8083)
echo    4. Conversion Svc    (port 8082)
echo    5. React Frontend    (port 3000)
echo.
echo  Each service loads its own .env file via run.bat.
echo.
pause

:: ── 1. Eureka Server ────────────────────────────────────────────────────────
echo.
echo  [1/5] Starting Eureka Server...
start "QMA :: Eureka Server  [8761]" cmd /k "color 0B && cd /d %ROOT%eureka-server && echo Starting Eureka Server... && run.bat"

echo  Waiting for Eureka to be ready (checking every 5s)...
:wait_eureka
timeout /t 5 /nobreak >nul
curl -s --max-time 3 http://localhost:8761/actuator/health >nul 2>&1
if errorlevel 1 goto wait_eureka
echo  [OK] Eureka Server is UP ^| http://localhost:8761

:: ── 2. API Gateway ──────────────────────────────────────────────────────────
echo.
echo  [2/5] Starting API Gateway...
start "QMA :: API Gateway    [8080]" cmd /k "color 0E && cd /d %ROOT%api-gateway && echo Starting API Gateway... && run.bat"

echo  Waiting for API Gateway to be ready...
:wait_gateway
timeout /t 5 /nobreak >nul
curl -s --max-time 3 http://localhost:8080/actuator/health >nul 2>&1
if errorlevel 1 goto wait_gateway
echo  [OK] API Gateway is UP ^| http://localhost:8080

:: ── 3. Auth Service ─────────────────────────────────────────────────────────
echo.
echo  [3/5] Starting Auth Service...
start "QMA :: Auth Service   [8083]" cmd /k "color 0D && cd /d %ROOT%auth-service && echo Starting Auth Service... && run.bat"

echo  Waiting for Auth Service to be ready...
:wait_auth
timeout /t 5 /nobreak >nul
curl -s --max-time 3 http://localhost:8083/api/auth/health >nul 2>&1
if errorlevel 1 goto wait_auth
echo  [OK] Auth Service is UP ^| http://localhost:8083

:: ── 4. Conversion Service ───────────────────────────────────────────────────
echo.
echo  [4/5] Starting Conversion Service...
start "QMA :: Conversion Svc [8082]" cmd /k "color 03 && cd /d %ROOT%conversion-service && echo Starting Conversion Service... && run.bat"

echo  Waiting for Conversion Service to be ready...
:wait_convert
timeout /t 5 /nobreak >nul
curl -s --max-time 3 http://localhost:8082/api/convert/health >nul 2>&1
if errorlevel 1 goto wait_convert
echo  [OK] Conversion Service is UP ^| http://localhost:8082

:: ── 5. React Frontend ───────────────────────────────────────────────────────
echo.
echo  [5/5] Starting React Frontend...
start "QMA :: Frontend        [3000]" cmd /k "color 0F && cd /d %ROOT%frontend && echo Installing dependencies... && npm install && echo Starting React app... && run.bat"

:: ── All done ─────────────────────────────────────────────────────────────────
echo.
echo  ============================================================
echo    All services started successfully!
echo  ============================================================
echo.
echo    Eureka Dashboard  :  http://localhost:8761
echo    Swagger UI        :  http://localhost:8080/swagger-ui.html
echo    API Gateway       :  http://localhost:8080
echo    Auth Service      :  http://localhost:8083
echo    Conversion Svc    :  http://localhost:8082
echo    Frontend (App)    :  http://localhost:3000
echo.
echo  Close this window to keep all services running.
echo  Run kill-ports.bat to stop everything.
echo.
pause
