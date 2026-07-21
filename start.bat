@echo off
echo =======================================================
echo ⚡ Inicializando entorno de EnergySec (Windows)...
echo =======================================================

if not exist "secrets" mkdir secrets

echo Generando secretos locales seguros a partir de plantillas...
if exist secrets\db_user.txt.example (copy /Y secrets\db_user.txt.example secrets\db_user.txt >nul) else (echo admin> secrets\db_user.txt)
if exist secrets\db_password.txt.example (copy /Y secrets\db_password.txt.example secrets\db_password.txt >nul) else (echo SecureP@ssw0rd!> secrets\db_password.txt)
if exist secrets\ml_api_key.txt.example (copy /Y secrets\ml_api_key.txt.example secrets\ml_api_key.txt >nul) else (echo secure-internal-api-key-12345> secrets\ml_api_key.txt)
if exist secrets\jwt_private_key.pem.example (copy /Y secrets\jwt_private_key.pem.example secrets\jwt_private_key.pem >nul) else (echo dummy> secrets\jwt_private_key.pem)
if exist secrets\jwt_public_key.pem.example (copy /Y secrets\jwt_public_key.pem.example secrets\jwt_public_key.pem >nul) else (echo dummy> secrets\jwt_public_key.pem)

echo.
echo 🐳 Compilando y levantando contenedores Docker...
docker-compose up -d --build

echo.
echo ✅ ¡Plataforma iniciada correctamente!
echo =======================================================
echo 📍 Dashboard Front-end : Haz doble clic en frontend\index.html
echo 📍 Swagger API Docs    : http://localhost:8080/swagger-ui.html
echo 📍 Grafana Dashboards  : http://localhost:3000 (Usuario: admin / Clave: admin)
echo =======================================================
pause
