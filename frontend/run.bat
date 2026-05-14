@echo off
:: ── Load .env (Create React App reads .env automatically, but set here too) ───
for /f "usebackq tokens=1,* delims==" %%A in (`findstr /v "^#" ".env" ^| findstr /v "^$"`) do (
    set "%%A=%%B"
)
:: ── Start frontend ────────────────────────────────────────────────────────────
npm start
