package com.example.authen.service;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class JwtService {
    @Autowired
    Environment env;

    public static final String USERNAME = "username";

    public String generateTokenLogin(String username) {
        String token = null;
        try {
            JWSSigner signer = new MACSigner(generateShareSecret());
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
            builder.claim(USERNAME, username);
            builder.expirationTime(generateExpirationDate());
            JWTClaimsSet claimsSet = builder.build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);
            token = signedJWT.serialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    private JWTClaimsSet getClaimsFromToken(String token) {
        JWTClaimsSet claimsSet = null;
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(generateShareSecret());
            if (signedJWT.verify(verifier)) {
                claimsSet = signedJWT.getJWTClaimsSet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claimsSet;
    }

    private Date generateExpirationDate() {
        int expire_time = Integer.parseInt(Objects.requireNonNull(env.getProperty("expire_time")));
        return new Date(System.currentTimeMillis() + expire_time);
    }

    private Date generateExpireDateFromToken(String token) {
        Date expiration = null;
        JWTClaimsSet claimsSet = getClaimsFromToken(token);
        expiration = claimsSet.getExpirationTime();
        return expiration;
    }

    public String getUsernameFromtoken(String token) {
        String username = null;
        try {
            JWTClaimsSet claimsSet = getClaimsFromToken(token);
            username = claimsSet.getStringClaim(USERNAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return username;
    }

    private byte[] generateShareSecret() {
        byte[] sharedSecret = new byte[32];
        String secret_key = env.getProperty("secret_key");
        sharedSecret = secret_key.getBytes();
        return sharedSecret;
    }

    private boolean isTokenExpired(String token) {
        Date expiration = generateExpireDateFromToken(token);
        return expiration.before(new Date());
    }

    public boolean validateTokenLogin(String token) {
        if (token == null || token.trim().length() == 0) {
            return false;
        }

        String username = getUsernameFromtoken(token);
        if (username == null || username.isEmpty()) {
            return false;
        }

        if (isTokenExpired(token)) {
            return false;
        }
        return true;
    }
}
