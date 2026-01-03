package duy.personalproject.taskmanagementsystem.auth.service.impl;

import duy.personalproject.taskmanagementsystem.auth.config.properties.JwtConfigProperties;
import duy.personalproject.taskmanagementsystem.auth.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.auth.model.response.TokenInfo;
import duy.personalproject.taskmanagementsystem.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final JwtConfigProperties jwtConfigProperties;
    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfigProperties.getSecretKey());
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public TokenInfo generateAccessToken(UserEntity user) {
        return buildToken(user, jwtConfigProperties.getAccessTokenExpirationInSecond());
    }

    @Override
    public TokenInfo generateRefreshToken(UserEntity user) {
        return buildToken(user, jwtConfigProperties.getRefreshTokenExpirationInSecond());
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return userDetails.getUsername().equals(username) && !isTokenExpired(token);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private TokenInfo buildToken(UserEntity userEntity, long expirationInSecond) {
        Instant now = Instant.now();
        Instant expirationTime = now.plusSeconds(expirationInSecond);

        String token = Jwts.builder()
                .subject(userEntity.getUsername())
                .claim("userId", userEntity.getId())
                .claim("email", userEntity.getEmail())
                .claim("role", userEntity.getRole().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expirationTime))
                .signWith(signingKey)
                .compact();

        return new TokenInfo(token, expirationTime.getEpochSecond());
    }
}
