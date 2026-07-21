.PHONY: up down build logs test init-secrets

up:
	docker-compose up -d

down:
	docker-compose down -v

build:
	docker-compose build

logs:
	docker-compose logs -f

test:
	cd backend-core && mvn test

init-secrets:
	mkdir -p secrets
	echo "admin" > secrets/db_user.txt
	echo "SecureP@ssw0rd!" > secrets/db_password.txt
	echo "secure-internal-api-key-12345" > secrets/ml_api_key.txt
	# Generate RSA keys for JWT
	openssl genpkey -algorithm RSA -out secrets/jwt_private_key.pem -pkeyopt rsa_keygen_bits:2048
	openssl rsa -pubout -in secrets/jwt_private_key.pem -out secrets/jwt_public_key.pem
	@echo "Secrets initialized in ./secrets/"

train-model:
	docker run --rm -v $(PWD)/ml-engine:/app -w /app python:3.11-slim bash -c "pip install pandas scikit-learn joblib numpy && python scripts/train_model.py sample_data.csv"
	@echo "Modelo entrenado con datos reales."
