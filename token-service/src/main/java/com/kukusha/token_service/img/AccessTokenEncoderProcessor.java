package com.kukusha.token_service.img;

import com.kukusha.token_service.model.TokenEncoderObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class AccessTokenEncoderProcessor implements TokenEncoderProcessor, RSAKeyGenerator, JwtEncoderProcessor {

    public AccessTokenEncoderProcessor() {
        String privateKey = loadResource("keys/access/private.pem");
        String publicKey = loadResource("keys/access/public.pem");
        this.rsaKey = getRSAKey(privateKey, publicKey, "RSA");
        this.encoder = jwtEncoder(rsaKey);
    }

    private String loadResource(String path) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalStateException("Resource not found: " + path);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load resource: " + path, e);
        }
    }

    @Override
    public TokenEncoderObject encObj() {
        return null;
    }
}
