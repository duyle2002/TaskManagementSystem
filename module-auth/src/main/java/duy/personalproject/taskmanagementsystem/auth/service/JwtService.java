package duy.personalproject.taskmanagementsystem.auth.service;

import duy.personalproject.taskmanagementsystem.auth.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.auth.model.response.TokenInfo;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    TokenInfo generateAccessToken(UserEntity user);

    TokenInfo generateRefreshToken(UserEntity user);

    String extractUsername(String token);

    boolean validateToken(String token, UserDetails userDetails);
}
