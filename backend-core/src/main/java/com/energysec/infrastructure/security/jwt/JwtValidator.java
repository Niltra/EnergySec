package com.energysec.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@Slf4j
public class JwtValidator {

    @Value("${app.jwt.public-key-path}")
    private String publicKeyPath;

    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            // In a real app, you might fetch this from a JWKS endpoint instead of a local file
            String keyContent = new String(Files.readAllBytes(Paths.get(publicKeyPath.replace("classpath:", "src/main/resources/"))))
                    .replaceAll("\\n", "")
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "");
            
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(keyContent));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.publicKey = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            log.warn("Could not load RSA public key. Authentication might fail. Error: {}", e.getMessage());
        }
    }

    public Claims validateToken(String token) {
        if (publicKey == null) return null;
        
        try {
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("Invalid JWT Signature", e);
            return null;
        }
    }
}
