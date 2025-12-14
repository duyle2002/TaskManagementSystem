package duy.personalproject.taskmanagementsystem.repository;

import duy.personalproject.taskmanagementsystem.model.entity.RefreshTokenEntity;
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
    @Query("SELECT rt FROM RefreshTokenEntity rt WHERE rt.token = :token AND rt.revokedAt IS NULL AND rt.expiresAt > CURRENT_TIMESTAMP ")
    Optional<RefreshTokenEntity> findByTokenAndRevokedAtIsNullAndExpiresAtAfterNow(String token);

    @Query("SELECT rt FROM RefreshTokenEntity rt WHERE rt.expiresAt < :expirationThreshold OR rt.revokedAt IS NOT NULL")
    Page<RefreshTokenEntity> findTokensToCleanUp(Instant expirationThreshold, Pageable pageable);
}
