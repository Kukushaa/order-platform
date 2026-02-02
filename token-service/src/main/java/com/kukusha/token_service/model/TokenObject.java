package com.kukusha.token_service.model;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

public abstract class TokenObject {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final KeyFactory keyFactory;
    private final RSAKey rsaJwk;

    public TokenObject(String privateKey, String publicKey) {
        this.keyFactory = createKeyFactory("RSA");
        this.rsaJwk = generateRSAKey(privateKey, publicKey);
        this.jwtEncoder = createJwtEncoder(createJWKSource(rsaJwk));
        this.jwtDecoder = createDecoder(publicKey);
    }

    public JwtEncoder getJwtEncoder() {
        return jwtEncoder;
    }

    public JwtDecoder getJwtDecoder() {
        return jwtDecoder;
    }

    public RSAKey getRsaJwk() {
        return rsaJwk;
    }

    private RSAKey generateRSAKey(String privateKeyPEM,
                                  String publicKeyPEM) {
        try {
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(getPrivateKeyPKCS8E(privateKeyPEM));
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(getPublicKeyX509(publicKeyPEM));

            return new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load RSA keys", e);
        }
    }

    private static X509EncodedKeySpec getPublicKeyX509(String publicKeyPEM) {
        String publicKeyContent = publicKeyPEM
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        return new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
    }

    private static PKCS8EncodedKeySpec getPrivateKeyPKCS8E(String privateKeyPEM) {
        String privateKeyContent = privateKeyPEM
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        return new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
    }

    private JwtDecoder createDecoder(String publicKeyPEM) {
        try {
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(getPublicKeyX509(publicKeyPEM));

            return NimbusJwtDecoder.withPublicKey(publicKey).build();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load RSA public key", e);
        }
    }

    private KeyFactory createKeyFactory(String rsa) {
        try {
            return KeyFactory.getInstance(rsa);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private JWKSource<SecurityContext> createJWKSource(RSAKey rsaKey) {
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    private JwtEncoder createJwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }
}
