package com.nounse.core.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.nounse.core.security.SecurityConstants.EXPIRATION_TIME;
import static com.nounse.core.security.SecurityConstants.SECRET;
import static com.nounse.core.security.SecurityConstants.ROLE_CLAIM_KEY;

@Component
public class SecurityUtil {

    @Value("${token.secret}")
    private String secret;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Logger log = LoggerFactory.getLogger(SecurityUtil.class);

    public SecurityUtil(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public boolean verifyPassword(String rawPassword, String encryptedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encryptedPassword);
    }

    public String generateTokenForUser(String userIdentifier, String role) {
        if (secret == null || secret.isBlank()) {
            secret = SECRET;
        }

        Date now = new Date(System.currentTimeMillis());
        String jwtToken = JWT.create()
                .withSubject(userIdentifier)
                .withIssuer("nounse.com")
                .withAudience("nounse.client.user")
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withNotBefore(now)
                .withIssuedAt(now)
                .withClaim(ROLE_CLAIM_KEY, role)
                .sign(HMAC512(secret.getBytes()));

        if (secret == SECRET) {
            log.warn("Generated token with default secret");
        }

        return jwtToken;
    }

    public String getSubjectFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();
    }

    public String getClaimFromToken(String token, String claim) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getClaim(claim).asString();
    }
}
