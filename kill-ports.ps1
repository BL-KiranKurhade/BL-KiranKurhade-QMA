$ports = @(8761, 8083, 8082, 8080, 5173, 3000)

Write-Host "========================================="
Write-Host "   QMA Microservices Port Killer"
Write-Host "========================================="

foreach ($port in $ports) {
    Write-Host "Checking port $port..." -NoNewline
    $connections = Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue
    
    if ($connections) {
        Write-Host " Found!" -ForegroundColor Yellow
        foreach ($conn in $connections) {
            $pidToKill = $conn.OwningProcess
            try {
                $processName = (Get-Process -Id $pidToKill).ProcessName
                Write-Host " -> Killing process $processName (PID: $pidToKill) on port $port..." -ForegroundColor Red
                Stop-Process -Id $pidToKill -Force -ErrorAction Stop
                Write-Host " -> Successfully terminated." -ForegroundColor Green
            } catch {
                Write-Host " -> Failed to kill process $pidToKill. It may require Administrator privileges." -ForegroundColor Red
            }
        }
    } else {
        Write-Host " Clear." -ForegroundColor Green
    }
}

Write-Host "========================================="
Write-Host "All specified ports have been cleared."
Write-Host "========================================="
