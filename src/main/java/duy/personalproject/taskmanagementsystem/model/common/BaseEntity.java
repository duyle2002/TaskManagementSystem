package duy.personalproject.taskmanagementsystem.model.common;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * Base entity class that provides common fields for all entities.
 * All entity classes should extend this class.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * Unique identifier for the entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    /**
     * Timestamp when the entity was created.
     * Automatically set by the database.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Timestamp when the entity was last updated.
     * Automatically updated by the database.
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Soft delete timestamp.
     * When this is not null, the entity is considered deleted.
     */
    @Column(name = "deleted_at")
    private Instant deletedAt;
}

