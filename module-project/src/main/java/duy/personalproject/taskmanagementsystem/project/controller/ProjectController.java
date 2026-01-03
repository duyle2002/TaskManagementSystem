package duy.personalproject.taskmanagementsystem.project.controller;

import duy.personalproject.taskmanagementsystem.core.annotation.LogExecutionTime;
import duy.personalproject.taskmanagementsystem.core.model.common.ApiResponse;
import duy.personalproject.taskmanagementsystem.core.model.common.PaginationResponse;
import duy.personalproject.taskmanagementsystem.core.model.constant.SecurityConstants;
import duy.personalproject.taskmanagementsystem.auth.security.CustomUserDetails;
import duy.personalproject.taskmanagementsystem.project.model.request.CreateProjectRequest;
import duy.personalproject.taskmanagementsystem.project.model.request.SearchProjectRequest;
import duy.personalproject.taskmanagementsystem.project.model.request.UpdateProjectRequest;
import duy.personalproject.taskmanagementsystem.project.model.response.ProjectResponse;
import duy.personalproject.taskmanagementsystem.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j(topic = "PROJECT_CONTROLLER")
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
@Tag(name = "Project Controller", description = "APIs for managing projects")
@LogExecutionTime
public class ProjectController {
    private final ProjectService projectService;

    @Operation(
            summary = "Create Project",
            description = "API to create a new project",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "201",
                            description = "Project created successfully"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Invalid request data"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    )
            }
    )
    @PreAuthorize(SecurityConstants.HAS_ROLE_USER_OR_ADMIN)
    @PostMapping
    public ApiResponse<ProjectResponse> create(@Valid @RequestBody CreateProjectRequest createProjectRequest, @AuthenticationPrincipal CustomUserDetails currentUser) {
        log.info("Starting create project with request: {}", createProjectRequest);
        ProjectResponse projectResponse = projectService.create(createProjectRequest, currentUser.getUserEntity().getId());
        return ApiResponse.created(projectResponse);
    }

    @Operation(
            summary = "Get Project by ID",
            description = "API to retrieve a project by its ID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Project retrieved successfully"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Project not found"
                    )
            }
    )
    @PreAuthorize(SecurityConstants.HAS_ROLE_USER_OR_ADMIN)
    @GetMapping("/{projectId}")
    public ApiResponse<ProjectResponse> findById(@PathVariable("projectId") UUID projectId) {
        log.info("Starting find project by id: {}", projectId);
        ProjectResponse projectResponse = projectService.findById(projectId);
        return ApiResponse.ok(projectResponse);
    }

    @Operation(
            summary = "Search projects",
            description = "API to search projects with pagination and sorting",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Projects retrieved successfully"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Invalid request data"
                    )
            }
    )
    @PreAuthorize(SecurityConstants.HAS_ROLE_USER_OR_ADMIN)
    @GetMapping
    public ApiResponse<PaginationResponse<ProjectResponse>> search(@ParameterObject SearchProjectRequest request) {
        log.info("Starting search projects with request: {}", request);
        PaginationResponse<ProjectResponse> projectResponses = projectService.searchProjects(request);
        return ApiResponse.ok(projectResponses);
    }

    @Operation(
            summary = "Update Project",
            description = "API to update an existing project",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Project updated successfully"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Invalid request data"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Project not found"
                    )
            }
    )
    @PreAuthorize(SecurityConstants.HAS_ROLE_USER_OR_ADMIN)
    @PutMapping("/{projectId}")
    public ApiResponse<ProjectResponse> update(@PathVariable UUID projectId, @Valid @RequestBody UpdateProjectRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
        log.info("Starting update project with id: {}", projectId);
        ProjectResponse projectResponse = projectService.update(projectId, request, currentUser.getUserEntity().getId());
        return ApiResponse.ok(projectResponse);
    }

    @DeleteMapping("/{projectId}")
    @PreAuthorize(SecurityConstants.HAS_ROLE_USER_OR_ADMIN)
    public ApiResponse<Void> delete(@PathVariable UUID projectId, @AuthenticationPrincipal CustomUserDetails currentUser) {
        log.info("Starting delete project with id: {}", projectId);
        projectService.delete(projectId, currentUser.getUserEntity().getId());
        return ApiResponse.okWithMessage("Project deleted successfully");
    }
}
