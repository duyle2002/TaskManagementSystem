package duy.personalproject.taskmanagementsystem.repository;

import duy.personalproject.taskmanagementsystem.model.entity.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, UUID> {

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM ProjectEntity p WHERE LOWER(p.name) = LOWER(:name) AND p.deletedAt IS NULL ")
    boolean existsByNameIgnoreCase(@Param("name") String name);

    @Query("SELECT p FROM ProjectEntity p WHERE p.id = :projectId AND p.owner.id = :ownerId AND p.deletedAt IS NULL")
    Optional<ProjectEntity> findByIdAndOwnerId(@Param("projectId") UUID projectId, @Param("ownerId") UUID ownerId);

    @Query("""
        SELECT p FROM ProjectEntity p
        WHERE (COALESCE(TRIM(:search), '') = '' OR
        LOWER(p.name) LIKE LOWER(CONCAT('%', COALESCE(TRIM(:search), ''), '%')) OR
        LOWER(p.description) LIKE LOWER(CONCAT('%', COALESCE(TRIM(:search), ''), '%'))) AND p.deletedAt IS NULL
        """
    )
    Page<ProjectEntity> searchProjects(@Param("search") String search, Pageable pageable);

    @Modifying
    @Query("UPDATE ProjectEntity p SET p.deletedAt = CURRENT_TIMESTAMP WHERE p.id = :projectId")
    void softDeleteById(UUID projectId);
}
