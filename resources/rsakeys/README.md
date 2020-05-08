# RSA-Keys

Generierung der KEYs

      openssl genrsa -out jwt.pem 2048
      openssl rsa -pubout -in jwt.pem -out jwt.pub.pem
      openssl pkcs8 -topk8 -in jwt.pem -inform pem -out jwt.pkcs8.pem -outform pem -nocrypt

