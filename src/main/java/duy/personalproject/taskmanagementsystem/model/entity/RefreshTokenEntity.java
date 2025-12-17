package duy.personalproject.taskmanagementsystem.model.entity;

import duy.personalproject.taskmanagementsystem.model.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Table(name = "refresh_tokens")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenEntity extends BaseEntity {
    @Column(name = "hashed_token", nullable = false, unique = true)
    private String hashedToken;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
