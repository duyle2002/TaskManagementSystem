package duy.personalproject.taskmanagementsystem.unit.service;

import duy.personalproject.taskmanagementsystem.exception.DuplicateResourceException;
import duy.personalproject.taskmanagementsystem.exception.ResourceNotFoundException;
import duy.personalproject.taskmanagementsystem.mapper.ProjectMapper;
import duy.personalproject.taskmanagementsystem.model.entity.ProjectEntity;
import duy.personalproject.taskmanagementsystem.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.model.request.project.CreateProjectRequest;
import duy.personalproject.taskmanagementsystem.model.request.project.UpdateProjectRequest;
import duy.personalproject.taskmanagementsystem.model.response.project.ProjectResponse;
import duy.personalproject.taskmanagementsystem.repository.ProjectRepository;
import duy.personalproject.taskmanagementsystem.repository.UserRepository;
import duy.personalproject.taskmanagementsystem.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProjectServiceImpl.
 * Uses Mockito to mock dependencies and test business logic in isolation.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProjectService Unit Tests")
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private UserEntity testUser;
    private ProjectEntity testProject;
    private CreateProjectRequest createRequest;
    private UpdateProjectRequest updateRequest;
    private ProjectResponse projectResponse;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(UUID.randomUUID());
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        testProject = new ProjectEntity();
        testProject.setId(UUID.randomUUID());
        testProject.setName("Test Project");
        testProject.setDescription("Test Description");
        testProject.setOwner(testUser);

        createRequest = new CreateProjectRequest("New Project", "New Description");
        updateRequest = new UpdateProjectRequest("Updated Project", "Updated Description");

        projectResponse = new ProjectResponse(
                testProject.getId(),
                "Test Project",
                "Test Description",
                testUser.getId(),
                null,
                null
        );
    }

    @Nested
    @DisplayName("Create Project Tests")
    class CreateProjectTests {

        @Test
        @DisplayName("Should create project successfully")
        void createProject_ValidRequest_ReturnsProjectResponse() {
            UUID userId = testUser.getId();
            when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
            when(projectRepository.existsByNameIgnoreCase(createRequest.name())).thenReturn(false);
            when(projectMapper.mapCreateRequestToEntity(createRequest)).thenReturn(testProject);
            when(projectRepository.save(any(ProjectEntity.class))).thenReturn(testProject);
            when(projectMapper.mapEntityToResponse(testProject)).thenReturn(projectResponse);

            ProjectResponse result = projectService.create(createRequest, userId);

            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo("Test Project");
            verify(userRepository).findById(userId);
            verify(projectRepository).existsByNameIgnoreCase(createRequest.name());
            verify(projectRepository).save(any(ProjectEntity.class));
            verify(projectMapper).mapEntityToResponse(testProject);
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void createProject_UserNotFound_ThrowsResourceNotFoundException() {
            UUID userId = UUID.randomUUID();
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> projectService.create(createRequest, userId))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(userRepository).findById(userId);
            verify(projectRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when project name already exists")
        void createProject_DuplicateName_ThrowsDuplicateResourceException() {
            UUID userId = testUser.getId();
            when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
            when(projectRepository.existsByNameIgnoreCase(createRequest.name())).thenReturn(true);

            assertThatThrownBy(() -> projectService.create(createRequest, userId))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("Project with the same name already exists");

            verify(projectRepository).existsByNameIgnoreCase(createRequest.name());
            verify(projectRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Update Project Tests")
    class UpdateProjectTests {

        @Test
        @DisplayName("Should update project successfully")
        void updateProject_ValidRequest_ReturnsUpdatedProject() {
            UUID projectId = testProject.getId();
            UUID userId = testUser.getId();
            when(projectRepository.findByIdAndOwnerId(projectId, userId))
                    .thenReturn(Optional.of(testProject));
            when(projectRepository.existsByNameIgnoreCase(updateRequest.name())).thenReturn(false);
            when(projectRepository.save(any(ProjectEntity.class))).thenReturn(testProject);
            when(projectMapper.mapEntityToResponse(testProject)).thenReturn(projectResponse);

            ProjectResponse result = projectService.update(projectId, updateRequest, userId);

            assertThat(result).isNotNull();
            verify(projectRepository).findByIdAndOwnerId(projectId, userId);
            verify(projectMapper).mapUpdateRequestToEntity(updateRequest, testProject);
            verify(projectRepository).save(testProject);
        }

        @Test
        @DisplayName("Should throw exception when project not found")
        void updateProject_ProjectNotFound_ThrowsResourceNotFoundException() {
            UUID projectId = UUID.randomUUID();
            UUID userId = UUID.randomUUID();
            when(projectRepository.findByIdAndOwnerId(projectId, userId))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> projectService.update(projectId, updateRequest, userId))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(projectRepository).findByIdAndOwnerId(projectId, userId);
            verify(projectRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when updating to existing project name")
        void updateProject_DuplicateName_ThrowsDuplicateResourceException() {
            UUID projectId = testProject.getId();
            UUID userId = testUser.getId();
            testProject.setName("Different Name"); // Make current name different
            when(projectRepository.findByIdAndOwnerId(projectId, userId))
                    .thenReturn(Optional.of(testProject));
            when(projectRepository.existsByNameIgnoreCase(updateRequest.name())).thenReturn(true);

            assertThatThrownBy(() -> projectService.update(projectId, updateRequest, userId))
                    .isInstanceOf(DuplicateResourceException.class);

            verify(projectRepository).existsByNameIgnoreCase(updateRequest.name());
            verify(projectRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Find Project Tests")
    class FindProjectTests {

        @Test
        @DisplayName("Should find project by id successfully")
        void findById_ExistingProject_ReturnsProjectResponse() {
            UUID projectId = testProject.getId();
            when(projectRepository.findById(projectId)).thenReturn(Optional.of(testProject));
            when(projectMapper.mapEntityToResponse(testProject)).thenReturn(projectResponse);

            ProjectResponse result = projectService.findById(projectId);

            assertThat(result).isNotNull();
            assertThat(result.id()).isEqualTo(projectId);
            verify(projectRepository).findById(projectId);
            verify(projectMapper).mapEntityToResponse(testProject);
        }

        @Test
        @DisplayName("Should throw exception when project not found")
        void findById_NonExistingProject_ThrowsResourceNotFoundException() {
            UUID projectId = UUID.randomUUID();
            when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> projectService.findById(projectId))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(projectRepository).findById(projectId);
            verify(projectMapper, never()).mapEntityToResponse(any());
        }
    }
}

