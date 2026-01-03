package duy.personalproject.taskmanagementsystem.project.service;

import duy.personalproject.taskmanagementsystem.core.model.common.PaginationResponse;
import duy.personalproject.taskmanagementsystem.project.model.request.CreateProjectRequest;
import duy.personalproject.taskmanagementsystem.project.model.request.SearchProjectRequest;
import duy.personalproject.taskmanagementsystem.project.model.request.UpdateProjectRequest;
import duy.personalproject.taskmanagementsystem.project.model.response.ProjectResponse;

import java.util.UUID;

public interface ProjectService {
    ProjectResponse create(CreateProjectRequest request, UUID userId);
    ProjectResponse update(UUID projectId, UpdateProjectRequest request, UUID userId);
    ProjectResponse findById(UUID projectId);
    PaginationResponse<ProjectResponse> searchProjects(SearchProjectRequest request);
    void delete(UUID projectId, UUID userId);
}
