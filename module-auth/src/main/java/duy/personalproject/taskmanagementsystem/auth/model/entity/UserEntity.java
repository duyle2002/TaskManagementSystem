package duy.personalproject.taskmanagementsystem.auth.model.entity;

import duy.personalproject.taskmanagementsystem.core.model.entity.BaseEntity;
import duy.personalproject.taskmanagementsystem.core.model.enums.UserRole;
import duy.personalproject.taskmanagementsystem.core.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity extends BaseEntity {
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private UserRole role = UserRole.ROLE_USER;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private UserStatus userStatus = UserStatus.ACTIVE;

    // Note: ProjectMemberEntity relationship is managed in module-project to avoid circular dependency
}
