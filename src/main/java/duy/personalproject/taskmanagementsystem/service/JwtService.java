package duy.personalproject.taskmanagementsystem.service;

import duy.personalproject.taskmanagementsystem.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.model.response.auth.TokenInfo;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    TokenInfo generateAccessToken(UserEntity user);

    TokenInfo generateRefreshToken(UserEntity user);

    String extractUsername(String token);

    boolean validateToken(String token, UserDetails userDetails);
}
