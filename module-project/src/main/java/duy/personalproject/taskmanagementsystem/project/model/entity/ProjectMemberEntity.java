package duy.personalproject.taskmanagementsystem.project.model.entity;

import duy.personalproject.taskmanagementsystem.auth.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.core.model.entity.BaseEntity;
import duy.personalproject.taskmanagementsystem.core.model.enums.ProjectMemberRole;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "project_members")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProjectMemberEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private ProjectEntity project;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectMemberRole role;
}
