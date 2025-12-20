package duy.personalproject.taskmanagementsystem.service;

import duy.personalproject.taskmanagementsystem.model.common.PaginationResponse;
import duy.personalproject.taskmanagementsystem.model.request.project.CreateProjectRequest;
import duy.personalproject.taskmanagementsystem.model.request.project.SearchProjectRequest;
import duy.personalproject.taskmanagementsystem.model.request.project.UpdateProjectRequest;
import duy.personalproject.taskmanagementsystem.model.response.project.ProjectResponse;

import java.util.UUID;

public interface ProjectService {
    ProjectResponse create(CreateProjectRequest request, UUID userId);
    ProjectResponse update(UUID projectId, UpdateProjectRequest request, UUID userId);
    ProjectResponse findById(UUID projectId);
    PaginationResponse<ProjectResponse> searchProjects(SearchProjectRequest request);
    void delete(UUID projectId, UUID userId);
}
