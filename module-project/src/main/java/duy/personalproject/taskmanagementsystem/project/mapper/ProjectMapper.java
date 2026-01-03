package duy.personalproject.taskmanagementsystem.project.mapper;

import duy.personalproject.taskmanagementsystem.project.model.entity.ProjectEntity;
import duy.personalproject.taskmanagementsystem.project.model.request.CreateProjectRequest;
import duy.personalproject.taskmanagementsystem.project.model.request.UpdateProjectRequest;
import duy.personalproject.taskmanagementsystem.project.model.response.ProjectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectMapper {
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "projectMembers", ignore = true)
    ProjectEntity mapCreateRequestToEntity(CreateProjectRequest request);

    @Mapping(target = "ownerId", source = "owner.id")
    ProjectResponse mapEntityToResponse(ProjectEntity projectEntity);

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "projectMembers", ignore = true)
    void mapUpdateRequestToEntity(UpdateProjectRequest request, @MappingTarget ProjectEntity existingEntity);
}
