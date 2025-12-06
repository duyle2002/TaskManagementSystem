package duy.personalproject.taskmanagementsystem.model.entity;

import duy.personalproject.taskmanagementsystem.model.common.BaseEntity;
import duy.personalproject.taskmanagementsystem.model.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity extends BaseEntity {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private Role role;
}
