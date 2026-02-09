#!/bin/bash

TYPE=$1

if [ -z "$TYPE" ]; then
  echo "Usage: $0 <type>"
  echo "Example: $0 access"
  exit 1
fi

mkdir -p "keys/$TYPE"

openssl genpkey -algorithm RSA -out "keys/$TYPE/private.pem" -pkeyopt rsa_keygen_bits:2048
openssl rsa -pubout -in "keys/$TYPE/private.pem" -out "keys/$TYPE/public.pem"

echo "Keys generated in keys/$TYPE/"
