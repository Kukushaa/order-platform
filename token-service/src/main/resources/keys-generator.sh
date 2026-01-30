# Generate access token keys
openssl genpkey -algorithm RSA -out keys/access/private.pem -pkeyopt rsa_keygen_bits:2048
openssl rsa -pubout -in keys/access/private.pem -out keys/access/public.pem

# Generate refresh token keys
openssl genpkey -algorithm RSA -out keys/refresh/private.pem -pkeyopt rsa_keygen_bits:2048
openssl rsa -pubout -in keys/refresh/private.pem -out keys/refresh/public.pem