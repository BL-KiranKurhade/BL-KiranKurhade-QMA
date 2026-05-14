@echo off
:: ── Load .env ─────────────────────────────────────────────────────────────────
for /f "usebackq tokens=1,* delims==" %%A in (`findstr /v "^#" ".env" ^| findstr /v "^$"`) do (
    set "%%A=%%B"
)
:: ── Start service ─────────────────────────────────────────────────────────────
mvn spring-boot:run
