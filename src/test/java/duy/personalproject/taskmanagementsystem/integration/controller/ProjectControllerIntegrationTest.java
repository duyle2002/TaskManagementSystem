package duy.personalproject.taskmanagementsystem.integration.controller;

import duy.personalproject.taskmanagementsystem.config.IntegrationTestBase;
import duy.personalproject.taskmanagementsystem.model.entity.ProjectEntity;
import duy.personalproject.taskmanagementsystem.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.model.enums.ProjectMemberRole;
import duy.personalproject.taskmanagementsystem.model.enums.UserRole;
import duy.personalproject.taskmanagementsystem.model.request.project.CreateProjectRequest;
import duy.personalproject.taskmanagementsystem.model.request.project.UpdateProjectRequest;
import duy.personalproject.taskmanagementsystem.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Project Controller Integration Tests")
class ProjectControllerIntegrationTest extends IntegrationTestBase {
    private static final String PROJECT_BASE_URL = "/api/v1/projects";
    @Autowired
    private ProjectRepository projectRepository;

    private String authToken;
    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = createTestUser("projectowner", "owner@example.com", UserRole.ROLE_USER);
        authToken = generateToken(testUser);
    }

    @Test
    @DisplayName("POST /api/v1/projects - Should create project successfully")
    void createProject_ValidRequest_ReturnsCreatedProject() throws Exception {
        CreateProjectRequest request = new CreateProjectRequest(
                "New Project",
                "Project Description"
        );

        mockMvc.perform(post(PROJECT_BASE_URL)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.data.name").value("New Project"))
                .andExpect(jsonPath("$.data.description").value("Project Description"))
                .andExpect(jsonPath("$.data.ownerId").value(testUser.getId().toString()));
    }

    @Test
    @DisplayName("POST /api/v1/projects - Should fail without authentication")
    void createProject_NoAuth_ReturnsUnauthorized() throws Exception {
        CreateProjectRequest request = new CreateProjectRequest("Project", "Description");

        mockMvc.perform(post(PROJECT_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/v1/projects - Should fail with duplicate name")
    void createProject_DuplicateName_ReturnsConflict() throws Exception {
        ProjectEntity existingProject = ProjectEntity.builder()
                .name("Existing Project")
                .description("Description")
                .owner(testUser)
                .build();
        existingProject.addMember(testUser, ProjectMemberRole.OWNER);
        projectRepository.save(existingProject);

        CreateProjectRequest request = new CreateProjectRequest(
                "Existing Project",
                "Another Description"
        );

        // When & Then
        mockMvc.perform(post(PROJECT_BASE_URL)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(409));
    }

    @Test
    @DisplayName("POST /api/v1/projects - Should fail with blank name")
    void createProject_BlankName_ReturnsBadRequest() throws Exception {
        CreateProjectRequest request = new CreateProjectRequest("", "Description");

        mockMvc.perform(post(PROJECT_BASE_URL)
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("GET /api/v1/projects/{id} - Should get project by id successfully")
    void getProject_ExistingProject_ReturnsProject() throws Exception {
        ProjectEntity project = ProjectEntity.builder()
                .name("Test Project")
                .description("Test Description")
                .owner(testUser)
                .build();
        project.addMember(testUser, ProjectMemberRole.OWNER);
        project = projectRepository.save(project);

        mockMvc.perform(get(PROJECT_BASE_URL + "/{projectId}", project.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(project.getId().toString()))
                .andExpect(jsonPath("$.data.name").value("Test Project"))
                .andExpect(jsonPath("$.data.description").value("Test Description"));
    }

    @Test
    @DisplayName("GET /api/v1/projects/{id} - Should return 404 for non-existent project")
    void getProject_NonExistent_ReturnsNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get(PROJECT_BASE_URL + "/{projectId}", nonExistentId)
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    @DisplayName("PUT /api/v1/projects/{id} - Should update project successfully")
    void updateProject_ValidRequest_ReturnsUpdatedProject() throws Exception {
        ProjectEntity project = ProjectEntity.builder()
                .name("Original Name")
                .description("Original Description")
                .owner(testUser)
                .build();
        project.addMember(testUser, ProjectMemberRole.OWNER);
        project = projectRepository.save(project);

        UpdateProjectRequest request = new UpdateProjectRequest(
                "Updated Name",
                "Updated Description"
        );

        mockMvc.perform(put(PROJECT_BASE_URL + "/{projectId}", project.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Updated Name"))
                .andExpect(jsonPath("$.data.description").value("Updated Description"));
    }

    @Test
    @DisplayName("PUT /api/v1/projects/{id} - Should fail when user is not owner")
    void updateProject_NotOwner_ReturnsNotFound() throws Exception {
        UserEntity anotherUser = createTestUser("anotheruser", "another@example.com", UserRole.ROLE_USER);
        ProjectEntity project = ProjectEntity.builder()
                .name("Another's Project")
                .description("Description")
                .owner(anotherUser)
                .build();

        project.addMember(anotherUser, ProjectMemberRole.OWNER);
        project = projectRepository.save(project);

        UpdateProjectRequest request = new UpdateProjectRequest("Updated", "Updated");

        mockMvc.perform(put("/api/v1/projects/{projectId}", project.getId())
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/v1/projects - Should search projects with pagination")
    void searchProjects_WithPagination_ReturnsPagedResults() throws Exception {
        for (int i = 1; i <= 5; i++) {
            ProjectEntity project = ProjectEntity.builder()
                    .name("Project " + i)
                    .description("Description " + i)
                    .owner(testUser)
                    .build();
            project.addMember(testUser, ProjectMemberRole.OWNER);
            projectRepository.save(project);
        }

        mockMvc.perform(get(PROJECT_BASE_URL)
                        .header("Authorization", "Bearer " + authToken)
                        .param("page", "0")
                        .param("size", "3")
                        .param("sortBy", "createdAt")
                        .param("sortDirection", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.items").isArray())
                .andExpect(jsonPath("$.data.items", hasSize(3)))
                .andExpect(jsonPath("$.data.metadata.totalItems").value(greaterThanOrEqualTo(5)))
                .andExpect(jsonPath("$.data.metadata.totalPages").value(greaterThanOrEqualTo(2)));
    }

    @Test
    @DisplayName("GET /api/v1/projects - Should search projects by keyword")
    void searchProjects_WithSearchKeyword_ReturnsFilteredResults() throws Exception {
        ProjectEntity project1 = ProjectEntity.builder()
                .name("Backend API")
                .description("REST API")
                .owner(testUser)
                .build();

        project1.addMember(testUser, ProjectMemberRole.OWNER);
        projectRepository.save(project1);

        ProjectEntity project2 = ProjectEntity.builder()
                .name("Frontend UI")
                .description("React App")
                .owner(testUser)
                .build();
        project2.addMember(testUser, ProjectMemberRole.OWNER);
        projectRepository.save(project2);

        mockMvc.perform(get(PROJECT_BASE_URL)
                        .header("Authorization", "Bearer " + authToken)
                        .param("search", "Backend"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.items").isArray())
                .andExpect(jsonPath("$.data.items[?(@.name == 'Backend API')]").exists());
    }

    @Test
    @DisplayName("DELETE /api/v1/projects/{id} - Should delete project successfully")
    void deleteProject_ExistingProject_ReturnsNoContent() throws Exception {
        ProjectEntity project = ProjectEntity.builder()
                .name("Project To Delete")
                .description("Description")
                .owner(testUser)
                .build();
        project.addMember(testUser, ProjectMemberRole.OWNER);
        project = projectRepository.save(project);

        mockMvc.perform(delete(PROJECT_BASE_URL + "/{projectId}", project.getId())
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().is2xxSuccessful());
    }
}

