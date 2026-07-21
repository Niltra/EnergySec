import jwt
import time

def generate_token():
    with open('secrets/jwt_private_key.pem', 'rb') as f:
        private_key = f.read()

    payload = {
        'sub': 'testuser',
        'roles': ['ROLE_ADMIN'],
        'iat': int(time.time()),
        'exp': int(time.time()) + 3600  # 1 hora
    }

    token = jwt.encode(payload, private_key, algorithm='RS256')
    print("\n--- TOKEN GENERADO ---")
    print(token)
    print("----------------------\n")

if __name__ == '__main__':
    generate_token()
