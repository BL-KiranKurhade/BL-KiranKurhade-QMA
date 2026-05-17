#!/bin/sh
set -e

# Dynamic substitution of API Gateway host inside compiled JS files (optional fallback)
if [ -n "$API_GATEWAY_HOST" ]; then
    API_URL="https://$API_GATEWAY_HOST"
    echo "[start.sh] Injecting dynamic API URL: $API_URL"
    find /usr/share/nginx/html/static/js -type f -name "*.js" \
        -exec sed -i "s|https://bl-kirankurhade-qma-32wf.onrender.com|$API_URL|g" {} \;
fi

exec nginx -g 'daemon off;'
