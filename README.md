# UC18 - Google Authentication & Spring Security JWT

## Setup
1. Create Google OAuth2 credentials at https://console.cloud.google.com/
2. Update `application.properties` with your CLIENT_ID and CLIENT_SECRET
3. Add redirect URI: `http://localhost:8081/login/oauth2/code/google`

## Run
```bash
mvn spring-boot:run
```

## Endpoints
- `POST /api/auth/login`  — local login with email/password → returns JWT
- `GET  /api/auth/me`     — current user (requires Bearer token)
- `GET  /oauth2/authorization/google` — start Google OAuth2 flow
- `GET  /api/auth/oauth2/success`     — OAuth2 callback → returns JWT

## Test JWT
```bash
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"password"}' | jq -r .token)
curl -H "Authorization: Bearer $TOKEN" http://localhost:8081/api/auth/me
```
