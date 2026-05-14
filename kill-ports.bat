@echo off
setlocal EnableDelayedExpansion
title QMA - Kill All Dev Ports
color 0C

echo.
echo  ============================================================
echo    QMA - Kill Dev Ports
echo    Stopping: 3000  8080  8082  8083  8761
echo  ============================================================
echo.

set PORTS=8761 8080 8083 8082 3000
set KILLED=0

for %%P in (%PORTS%) do (
    echo  Checking port %%P...
    set FOUND=0
    for /f "tokens=5" %%A in ('netstat -aon ^| findstr ":%%P " ^| findstr "LISTENING" 2^>nul') do (
        set PID=%%A
        if not "%%A"=="0" (
            echo    Killing PID %%A on port %%P
            taskkill /F /PID %%A >nul 2>&1
            if !errorlevel! == 0 (
                echo    [OK] Port %%P freed
                set /a KILLED+=1
            ) else (
                echo    [!!] Could not kill PID %%A - try running as Administrator
            )
            set FOUND=1
        )
    )
    if "!FOUND!"=="0" echo    [--] Port %%P was not in use
)

echo.
echo  ============================================================
if %KILLED% == 0 (
    echo    No processes were killed - all ports were already free.
) else (
    echo    Done! Freed %KILLED% port(s).
)
echo  ============================================================
echo.
pause
