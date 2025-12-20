package duy.personalproject.taskmanagementsystem.model.entity;

import duy.personalproject.taskmanagementsystem.model.common.BaseEntity;
import duy.personalproject.taskmanagementsystem.model.enums.ProjectMemberRole;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "project_members")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMemberEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectMemberRole role;
}
