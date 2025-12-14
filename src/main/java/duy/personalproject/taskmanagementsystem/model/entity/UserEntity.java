package duy.personalproject.taskmanagementsystem.model.entity;

import duy.personalproject.taskmanagementsystem.model.common.BaseEntity;
import duy.personalproject.taskmanagementsystem.model.enums.UserRole;
import duy.personalproject.taskmanagementsystem.model.enums.UserStatus;
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

    @Column(name = "fullName", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.ROLE_USER;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus userStatus = UserStatus.ACTIVE;
}
