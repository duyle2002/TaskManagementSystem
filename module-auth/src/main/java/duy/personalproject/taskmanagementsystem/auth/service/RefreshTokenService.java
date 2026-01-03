package duy.personalproject.taskmanagementsystem.auth.service;

import duy.personalproject.taskmanagementsystem.auth.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.auth.model.response.TokenInfo;

public interface RefreshTokenService {
    TokenInfo createRefreshToken(UserEntity user);
    UserEntity validateAndRetrieveUser(String refreshToken);
    void revokeRefreshToken(String refreshToken);
    void cleanUpExpiredAndRevokedTokens();
}
