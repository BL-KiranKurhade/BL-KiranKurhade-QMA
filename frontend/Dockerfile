# ================================================================
# QMA React Frontend  |  Port 80 (nginx)
# Multi-stage build: Node builder -> Nginx production server
# ================================================================

# ---- Stage 1: Build ------------------------------------------------
FROM node:18-alpine AS builder

WORKDIR /app

# Copy package files first for better Docker layer caching
COPY package.json package-lock.json* ./

# Install all dependencies
RUN npm install

# Copy source code
COPY . .

# Build production bundle

RUN npm run build

# ---- Stage 2: Serve with Nginx -------------------------------------
FROM nginx:1.25-alpine AS runtime

# Remove default nginx config
RUN rm /etc/nginx/conf.d/default.conf

# Copy custom nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy built React app from builder
COPY --from=builder /app/build /usr/share/nginx/html

# Copy startup script (injects API_GATEWAY_HOST at runtime)
COPY start.sh /start.sh
RUN chmod +x /start.sh

# Expose HTTP port
EXPOSE 80

# Healthcheck
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
    CMD wget -qO- http://localhost:80/ || exit 1

# start.sh replaces the baked-in localhost API URL with the real
# gateway URL ($API_GATEWAY_HOST), then launches nginx.
CMD ["/start.sh"]
