package duy.personalproject.taskmanagementsystem.service.impl;

import duy.personalproject.taskmanagementsystem.exception.DuplicateResourceException;
import duy.personalproject.taskmanagementsystem.exception.ErrorCode;
import duy.personalproject.taskmanagementsystem.exception.ResourceNotFoundException;
import duy.personalproject.taskmanagementsystem.mapper.ProjectMapper;
import duy.personalproject.taskmanagementsystem.model.common.PaginationResponse;
import duy.personalproject.taskmanagementsystem.model.entity.ProjectEntity;
import duy.personalproject.taskmanagementsystem.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.model.enums.ProjectMemberRole;
import duy.personalproject.taskmanagementsystem.model.enums.SortDirection;
import duy.personalproject.taskmanagementsystem.model.request.project.CreateProjectRequest;
import duy.personalproject.taskmanagementsystem.model.request.project.SearchProjectRequest;
import duy.personalproject.taskmanagementsystem.model.request.project.UpdateProjectRequest;
import duy.personalproject.taskmanagementsystem.model.response.project.ProjectResponse;
import duy.personalproject.taskmanagementsystem.repository.ProjectRepository;
import duy.personalproject.taskmanagementsystem.repository.UserRepository;
import duy.personalproject.taskmanagementsystem.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j(topic = "PROJECT_SERVICE")
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional
    public ProjectResponse create(CreateProjectRequest request, UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with id {} not found", userId);
                    return new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage());
                });

        validateProjectNameUniqueness(request.name());

        ProjectEntity projectEntity = projectMapper.mapCreateRequestToEntity(request);

        projectEntity.setOwner(user);
        projectEntity.addMember(user, ProjectMemberRole.OWNER);
        projectEntity = projectRepository.save(projectEntity);

        return projectMapper.mapEntityToResponse(projectEntity);
    }

    @Override
    @Transactional
    public ProjectResponse update(UUID projectId, UpdateProjectRequest request, UUID userId) {
        ProjectEntity projectEntity = validateProjectIdAndOwner(projectId, userId);

        if (!request.name().equals(projectEntity.getName())) {
            validateProjectNameUniqueness(request.name());
        }

        projectMapper.mapUpdateRequestToEntity(request, projectEntity);

        projectEntity = projectRepository.save(projectEntity);

        return projectMapper.mapEntityToResponse(projectEntity);
    }

    @Override
    public ProjectResponse findById(UUID projectId) {
        ProjectEntity projectEntity = projectRepository.findById(projectId).orElseThrow(() -> {
            log.error("Project with id {} not found", projectId);
            return new ResourceNotFoundException(ErrorCode.PROJECT_NOT_FOUND.getMessage());
        });

        return projectMapper.mapEntityToResponse(projectEntity);
    }

    @Override
    public PaginationResponse<ProjectResponse> searchProjects(SearchProjectRequest request) {
        Sort.Direction direction = request.sortDirection() == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, request.sortBy());

        Pageable pageable = PageRequest.of(request.page(), request.size(), sort);

        Page<ProjectEntity> projectEntityPage = projectRepository.searchProjects(request.search(), pageable);

        return PaginationResponse.of(projectEntityPage, projectMapper::mapEntityToResponse);
    }

    @Transactional
    @Override
    public void delete(UUID projectId, UUID userId) {
        ProjectEntity projectEntity = validateProjectIdAndOwner(projectId, userId);

        projectRepository.delete(projectEntity);
    }


    private void validateProjectNameUniqueness(String name) {
        if (projectRepository.existsByNameIgnoreCase(name)) {
            log.error("Project with name {} already exists", name);
            throw new DuplicateResourceException("Project with the same name already exists");
        }
    }

    private ProjectEntity validateProjectIdAndOwner(UUID projectId, UUID ownerId) {
        return projectRepository.findByIdAndOwnerId(projectId, ownerId).orElseThrow(() -> {
            log.error("Project with id {} not found for owner with id {}", projectId, ownerId);
            return new ResourceNotFoundException(ErrorCode.PROJECT_NOT_FOUND.getMessage());
        });
    }
}
