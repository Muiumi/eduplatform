package ru.rtstudy.educplatformsecurity.auth.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.rtstudy.educplatformsecurity.auth.JwtService;
import ru.rtstudy.educplatformsecurity.model.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    @Override
    public String extractUser(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, expiration);
    }

    @Override
    public String generateRefreshToken(User user) {
        return generateToken(new HashMap<>(), user, refreshTokenExpiration);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, long expirationTime) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUser(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
