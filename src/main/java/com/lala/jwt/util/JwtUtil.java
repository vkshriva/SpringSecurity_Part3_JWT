package com.lala.jwt.util;

import io.jsonwebtoken.Claims;  //Used to access the data  in a JWT token.
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;    //Used to build and parse JWT tokens.
import io.jsonwebtoken.security.Keys; // Help generate secure secrets keys for signing JWTs.
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

/*
    * This class is responsible for generating and validating JWT tokens.
    * It generates,parses and validates JWT tokens using a secret key.
    * It used by AuthService class to generate JWT Tokens and by JWTRequestFilter class to validate JWT tokens in incoming requests.
    *
    * 1.Creates JWTs for authenticated users (after successful login via POST
         /api/auth/login).
      2.Extracts information (e.g., email) from JWTs provided in requests (e.g., GET
          /api/account).
      3.Validates JWTs to ensure they are authentic, unexpired, and match the expected
      user.
 */


@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        SecretKey sKey= Keys.hmacShaKeyFor(secret.getBytes());
        return sKey;
    }
    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60)) // 1 min
                .signWith(getSigningKey())
                .claim("roles", roles) // Add role as a claim
                .compact();
    }
    public Claims extractClaims(String token) {
        JwtParser jParser = Jwts.parserBuilder().setSigningKey(getSigningKey()).build();
        //Here is where we parse the JWT token,verifies its signature.Throws Signature
        //Throws exception SignatureException or ExpiredJwtException if invalid or expired
        Jws<Claims> jwsClaims = jParser.parseClaimsJws(token);
        return jwsClaims.getBody();
    }
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
    public boolean validateToken(String token, String email) {
        return (extractEmail(token).equals(email) && !isTokenExpired(token));
    }
    public List<String> extractRoles(String token) {
        Claims claims = extractClaims(token);
        // Extract roles from the claims, assuming roles are stored as a list
         return (List<String>) claims.get("roles");

    }
}
