package com.example.blog.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    // Static secret key used for signing and validating JWT
    private static final String SECRET_KEY = "oiuytrdfgy65rfghy65edghy6ewsxcvhytrdcv9786ihigugyfhjc"; // Change this to your secret key
    private final SecretKey secretKey;

    public JwtUtil() {
        // Use a fixed secret key for both signing and validation
        this.secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        //System.out.println("Key Generated: " + Base64.getEncoder().encodeToString(secretKey.getEncoded())); // Print key on startup
    }

    // Method to generate JWT token
    public String generateToken(String subject) {
        System.out.println("Generating with Key: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Token expires after 1 hour
                .signWith(secretKey, SignatureAlgorithm.HS256) // Sign the JWT with the static secret key
                .compact();
    }

    // Method to validate the JWT token
    public boolean validateToken(String token) {
        System.out.println("Validating Token: " + token);
        System.out.println("Validating with Key: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token); // Validate the token using the secret key
            return true;
        } catch (SignatureException e) {
            System.err.println("Invalid signature: " + e.getMessage());
            return false; // If signature doesn't match
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return false; // If any other exception occurs
        }
    }

    // Method to extract claims from the JWT token
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    // Method to extract the subject (user) from the JWT token
    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }

    // Getter method for the secret key
    public SecretKey getSecretKey() {
        return this.secretKey;
    }
}
