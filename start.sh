#!/bin/bash
echo "⚡ Inicializando entorno de EnergySec..."
mkdir -p secrets

echo "Generando secretos locales seguros..."
cp secrets/db_user.txt.example secrets/db_user.txt 2>/dev/null || echo "admin" > secrets/db_user.txt
cp secrets/db_password.txt.example secrets/db_password.txt 2>/dev/null || echo "SecureP@ssw0rd!" > secrets/db_password.txt
cp secrets/ml_api_key.txt.example secrets/ml_api_key.txt 2>/dev/null || echo "secure-internal-api-key-12345" > secrets/ml_api_key.txt
cp secrets/jwt_private_key.pem.example secrets/jwt_private_key.pem 2>/dev/null || echo "dummy" > secrets/jwt_private_key.pem
cp secrets/jwt_public_key.pem.example secrets/jwt_public_key.pem 2>/dev/null || echo "dummy" > secrets/jwt_public_key.pem

echo "🐳 Levantando la infraestructura (Java, Python, Postgres, Prometheus, Grafana)..."
docker-compose up -d --build

echo "✅ ¡Todo listo!"
echo "📍 Abre el dashboard en tu navegador: frontend/index.html"
echo "📍 Swagger API Docs: http://localhost:8080/swagger-ui.html"
echo "📍 Grafana: http://localhost:3000 (admin / admin)"
