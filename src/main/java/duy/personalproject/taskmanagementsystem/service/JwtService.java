package duy.personalproject.taskmanagementsystem.service;

import duy.personalproject.taskmanagementsystem.model.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateAccessToken(UserEntity user);

    String generateRefreshToken(UserEntity user);

    String extractUsername(String token);

    boolean validateToken(String token, UserDetails userDetails);
}
