package duy.personalproject.taskmanagementsystem.service.impl;

import duy.personalproject.taskmanagementsystem.config.properties.RefreshTokenConfigProperties;
import duy.personalproject.taskmanagementsystem.exception.UnauthorizedException;
import duy.personalproject.taskmanagementsystem.model.entity.RefreshTokenEntity;
import duy.personalproject.taskmanagementsystem.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.model.response.auth.TokenInfo;
import duy.personalproject.taskmanagementsystem.repository.RefreshTokenRepository;
import duy.personalproject.taskmanagementsystem.service.JwtService;
import duy.personalproject.taskmanagementsystem.service.RefreshTokenService;
import duy.personalproject.taskmanagementsystem.util.TokenHashUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static duy.personalproject.taskmanagementsystem.model.constant.TimeConstants.DAY_IN_MILLISECONDS;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "REFRESH_TOKEN_SERVICE")
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenConfigProperties refreshTokenConfigProperties;

    @Override
    public TokenInfo createRefreshToken(UserEntity user) {
        TokenInfo refreshToken = jwtService.generateRefreshToken(user);
        String hashedToken = TokenHashUtil.hashToken(refreshToken.getToken());
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .hashedToken(hashedToken)
                .expiresAt(Instant.ofEpochSecond(refreshToken.getExpiresAt()))
                .user(user)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
        return refreshToken;
    }

    @Override
    public UserEntity validateAndRetrieveUser(String refreshToken) {
        String hashedToken = TokenHashUtil.hashToken(refreshToken);

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByTokenAndRevokedAtIsNullAndExpiresAtAfterNow(hashedToken)
                .orElseThrow(() -> {
                    log.error("Refresh token could not be found");
                    return new UnauthorizedException("Invalid or expired refresh token");
                });
        return refreshTokenEntity.getUser();
    }

    @Override
    public void revokeRefreshToken(String refreshToken) {
        String hashedToken = TokenHashUtil.hashToken(refreshToken);
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByTokenAndRevokedAtIsNullAndExpiresAtAfterNow(hashedToken)
                .orElseThrow(() -> {
                    log.error("Refresh token could not be found");
                    return new UnauthorizedException("Invalid or expired refresh token");
                });

        refreshTokenEntity.setRevokedAt(Instant.now());
        refreshTokenRepository.save(refreshTokenEntity);
    }

    /**
     * Cleans up expired and revoked refresh tokens from the database.
     * This method retrieves tokens that are either revoked or expired beyond the configured stale time,
     * and deletes them in batches to optimize performance.
     */
    @Override
    @Transactional
    public void cleanUpExpiredAndRevokedTokens() {
        log.info("Starting cleanup of expired and revoked refresh tokens");

        Instant now = Instant.now();
        Instant expirationThreshold = now.minusMillis(refreshTokenConfigProperties.getStaleTimeInDays() * DAY_IN_MILLISECONDS);
        PageRequest pageRequest = PageRequest.of(0, refreshTokenConfigProperties.getCleanupBatchSize());
        Page<RefreshTokenEntity> tokensToCleanUp = refreshTokenRepository.findTokensToCleanUp(expirationThreshold, pageRequest);
        int totalDeleted = 0;

        while (tokensToCleanUp.hasContent()) {
            refreshTokenRepository.deleteAllInBatch(tokensToCleanUp.getContent());
            totalDeleted += tokensToCleanUp.getNumberOfElements();
            tokensToCleanUp = refreshTokenRepository.findTokensToCleanUp(expirationThreshold, pageRequest);
        }

        log.info("Cleaned up {} expired and revoked refresh tokens", totalDeleted);
    }
}
