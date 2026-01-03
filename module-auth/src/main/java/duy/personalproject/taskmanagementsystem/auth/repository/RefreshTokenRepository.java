package duy.personalproject.taskmanagementsystem.auth.repository;

import duy.personalproject.taskmanagementsystem.auth.model.entity.RefreshTokenEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    @Query("SELECT rt FROM RefreshTokenEntity rt WHERE rt.hashedToken = :hashedToken AND rt.revokedAt IS NULL AND rt.expiresAt > CURRENT_TIMESTAMP AND rt.deletedAt IS NULL")
    Optional<RefreshTokenEntity> findByTokenAndRevokedAtIsNullAndExpiresAtAfterNow(String hashedToken);

    @Query("SELECT rt FROM RefreshTokenEntity rt WHERE (rt.expiresAt < :expirationThreshold OR rt.revokedAt IS NOT NULL) AND rt.deletedAt IS NULL")
    Page<RefreshTokenEntity> findTokensToCleanUp(Instant expirationThreshold, Pageable pageable);
}
