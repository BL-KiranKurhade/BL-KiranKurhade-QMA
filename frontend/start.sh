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

# Fallback to the production gateway hostname if the environment variable is not set on Render
API_GATEWAY_HOST=${API_GATEWAY_HOST:-bl-kirankurhade-qma-32wf.onrender.com}

if [ -n "$API_GATEWAY_HOST" ]; then
    API_URL="https://$API_GATEWAY_HOST"
    echo "[start.sh] Injecting API URL into JS: $API_URL"

    # Replace the build-time placeholder in every compiled JS chunk (fallback)
    find /usr/share/nginx/html/static/js -type f -name "*.js" \
        -exec sed -i "s|http://localhost:8080|$API_URL|g" {} \;

    echo "[start.sh] Injecting API Gateway Host into Nginx Config: $API_GATEWAY_HOST"
    # Replace the Nginx proxy_pass placeholder
    sed -i "s|API_GATEWAY_HOST|$API_GATEWAY_HOST|g" /etc/nginx/conf.d/default.conf

    echo "[start.sh] Runtime injections complete."
else
    echo "[start.sh] API_GATEWAY_HOST not set — using local defaults."
fi

exec nginx -g 'daemon off;'
