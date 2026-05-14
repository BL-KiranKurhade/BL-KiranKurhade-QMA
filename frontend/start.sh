#!/bin/sh
# ============================================================
# QMA Frontend — Container startup script
#
# React bundles the API URL at build time (REACT_APP_API_URL).
# On Render, we inject the real gateway URL at runtime by
# replacing the baked-in "http://localhost:8080" placeholder
# inside the compiled JS files.
#
# Set API_GATEWAY_HOST to your gateway's hostname, e.g.:
#   qma-gateway.onrender.com
# ============================================================
set -e

if [ -n "$API_GATEWAY_HOST" ]; then
    API_URL="https://$API_GATEWAY_HOST"
    echo "[start.sh] Injecting API URL: $API_URL"

    # Replace the build-time placeholder in every compiled JS chunk
    find /usr/share/nginx/html/static/js -type f -name "*.js" \
        -exec sed -i "s|http://localhost:8080|$API_URL|g" {} \;

    echo "[start.sh] API URL injection complete."
else
    echo "[start.sh] API_GATEWAY_HOST not set — using baked-in URL (http://localhost:8080)."
fi

exec nginx -g 'daemon off;'
