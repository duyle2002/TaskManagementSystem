package duy.personalproject.taskmanagementsystem.model.entity;

import duy.personalproject.taskmanagementsystem.model.common.BaseEntity;
import duy.personalproject.taskmanagementsystem.model.enums.ProjectMemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Table(name = "projects", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectEntity extends BaseEntity {
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private UserEntity owner;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ProjectMemberEntity> projectMembers = new HashSet<>();

    public void addMember(UserEntity userEntity, ProjectMemberRole role) {
        ProjectMemberEntity projectMember = ProjectMemberEntity.builder()
                .user(userEntity)
                .project(this)
                .role(role)
                .build();
        projectMembers.add(projectMember);
    }
}
