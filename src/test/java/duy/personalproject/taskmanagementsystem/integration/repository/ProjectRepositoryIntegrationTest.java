package duy.personalproject.taskmanagementsystem.integration.repository;

import duy.personalproject.taskmanagementsystem.config.IntegrationTestBase;
import duy.personalproject.taskmanagementsystem.model.entity.ProjectEntity;
import duy.personalproject.taskmanagementsystem.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.model.enums.ProjectMemberRole;
import duy.personalproject.taskmanagementsystem.model.enums.UserRole;
import duy.personalproject.taskmanagementsystem.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for ProjectRepository.
 * Tests custom queries and database operations.
 */
@DisplayName("Project Repository Integration Tests")
class ProjectRepositoryIntegrationTest extends IntegrationTestBase {

    @Autowired
    private ProjectRepository projectRepository;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = createTestUser("repotest", "repotest@example.com", UserRole.ROLE_USER);
    }

    @Test
    @DisplayName("Should check if project exists by name (case insensitive)")
    void existsByNameIgnoreCase_ExistingProject_ReturnsTrue() {
        ProjectEntity project = new ProjectEntity();
        project.setName("Test Project");
        project.setDescription("Description");
        project.setOwner(testUser);
        project.addMember(testUser, ProjectMemberRole.OWNER);
        projectRepository.save(project);

        boolean existsLowerCase = projectRepository.existsByNameIgnoreCase("test project");
        boolean existsUpperCase = projectRepository.existsByNameIgnoreCase("TEST PROJECT");
        boolean existsMixedCase = projectRepository.existsByNameIgnoreCase("TeSt PrOjEcT");

        assertThat(existsLowerCase).isTrue();
        assertThat(existsUpperCase).isTrue();
        assertThat(existsMixedCase).isTrue();
    }

    @Test
    @DisplayName("Should return false when project name doesn't exist")
    void existsByNameIgnoreCase_NonExistentProject_ReturnsFalse() {
        boolean exists = projectRepository.existsByNameIgnoreCase("Non Existent Project");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should find project by id and owner id")
    void findByIdAndOwnerId_ExistingProject_ReturnsProject() {
        ProjectEntity project = new ProjectEntity();
        project.setName("Owner's Project");
        project.setDescription("Description");
        project.setOwner(testUser);
        project.addMember(testUser, ProjectMemberRole.OWNER);
        project = projectRepository.save(project);

        Optional<ProjectEntity> found = projectRepository.findByIdAndOwnerId(
                project.getId(),
                testUser.getId()
        );

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Owner's Project");
        assertThat(found.get().getOwner().getId()).isEqualTo(testUser.getId());
    }

    @Test
    @DisplayName("Should not find project with wrong owner id")
    void findByIdAndOwnerId_WrongOwner_ReturnsEmpty() {
        UserEntity anotherUser = createTestUser("another", "another@example.com", UserRole.ROLE_USER);
        ProjectEntity project = new ProjectEntity();
        project.setName("Another's Project");
        project.setDescription("Description");
        project.setOwner(anotherUser);
        project.addMember(anotherUser, ProjectMemberRole.OWNER);
        project = projectRepository.save(project);

        Optional<ProjectEntity> found = projectRepository.findByIdAndOwnerId(
                project.getId(),
                testUser.getId()
        );

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should search projects by name")
    void searchProjects_ByName_ReturnsMatchingProjects() {
        createProject("Backend API", "REST API");
        createProject("Frontend UI", "React App");
        createProject("Backend Service", "Microservice");

        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ProjectEntity> results = projectRepository.searchProjects("Backend", pageable);

        assertThat(results.getContent())
                .hasSize(2)
                .extracting(ProjectEntity::getName)
                .containsExactlyInAnyOrder("Backend API", "Backend Service");
    }

    @Test
    @DisplayName("Should search projects by description")
    void searchProjects_ByDescription_ReturnsMatchingProjects() {
        createProject("Project A", "REST API Service");
        createProject("Project B", "GraphQL API Service");
        createProject("Project C", "Database Migration");

        PageRequest pageable = PageRequest.of(0, 10);
        Page<ProjectEntity> results = projectRepository.searchProjects("API", pageable);

        assertThat(results.getContent())
                .hasSize(2)
                .extracting(ProjectEntity::getDescription)
                .allMatch(desc -> desc.contains("API"));
    }

    @Test
    @DisplayName("Should return all projects when search is empty")
    void searchProjects_EmptySearch_ReturnsAllProjects() {
        createProject("Project 1", "Description 1");
        createProject("Project 2", "Description 2");
        createProject("Project 3", "Description 3");

        PageRequest pageable = PageRequest.of(0, 10);
        Page<ProjectEntity> results = projectRepository.searchProjects("", pageable);

        assertThat(results.getContent()).hasSize(3);
    }

    @Test
    @DisplayName("Should return all projects when search is null")
    void searchProjects_NullSearch_ReturnsAllProjects() {
        createProject("Project 1", "Description 1");
        createProject("Project 2", "Description 2");

        PageRequest pageable = PageRequest.of(0, 10);
        Page<ProjectEntity> results = projectRepository.searchProjects(null, pageable);

        assertThat(results.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("Should support pagination in search")
    void searchProjects_WithPagination_ReturnsPagedResults() {
        for (int i = 1; i <= 5; i++) {
            createProject("Project " + i, "Description " + i);
        }

        PageRequest pageable = PageRequest.of(0, 2);
        Page<ProjectEntity> page1 = projectRepository.searchProjects("", pageable);

        assertThat(page1.getContent()).hasSize(2);
        assertThat(page1.getTotalElements()).isEqualTo(5);
        assertThat(page1.getTotalPages()).isEqualTo(3);
        assertThat(page1.hasNext()).isTrue();

        pageable = PageRequest.of(1, 2);
        Page<ProjectEntity> page2 = projectRepository.searchProjects("", pageable);

        assertThat(page2.getContent()).hasSize(2);
        assertThat(page2.hasNext()).isTrue();
    }

    @Test
    @DisplayName("Should not return soft-deleted projects")
    void searchProjects_ExcludesSoftDeleted_ReturnsOnlyActiveProjects() {
        createProject("Active Project", "Active");
        ProjectEntity deletedProject = createProject("Deleted Project", "Deleted");

        deletedProject.setDeletedAt(java.time.Instant.now());
        projectRepository.save(deletedProject);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<ProjectEntity> results = projectRepository.searchProjects("", pageable);

        assertThat(results.getContent())
                .hasSize(1)
                .extracting(ProjectEntity::getName)
                .containsOnly("Active Project");
    }

    @Test
    @DisplayName("Should search case-insensitive")
    void searchProjects_CaseInsensitive_ReturnsMatches() {
        createProject("Backend Service", "Description");

        PageRequest pageable = PageRequest.of(0, 10);
        Page<ProjectEntity> results = projectRepository.searchProjects("backend", pageable);

        assertThat(results.getContent())
                .hasSize(1)
                .extracting(ProjectEntity::getName)
                .containsOnly("Backend Service");
    }

    private ProjectEntity createProject(String name, String description) {
        ProjectEntity project = new ProjectEntity();
        project.setName(name);
        project.setDescription(description);
        project.setOwner(testUser);
        project.addMember(testUser, ProjectMemberRole.OWNER);
        return projectRepository.save(project);
    }
}

