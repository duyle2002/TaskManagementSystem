package duy.personalproject.taskmanagementsystem.service;

import duy.personalproject.taskmanagementsystem.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.model.response.auth.TokenInfo;

public interface RefreshTokenService {
    TokenInfo createRefreshToken(UserEntity user);
    UserEntity validateAndRetrieveUser(String refreshToken);
    void revokeRefreshToken(String refreshToken);
    void cleanUpExpiredAndRevokedTokens();
}
