package duy.personalproject.taskmanagementsystem.mapper;

import duy.personalproject.taskmanagementsystem.model.entity.ProjectEntity;
import duy.personalproject.taskmanagementsystem.model.request.project.CreateProjectRequest;
import duy.personalproject.taskmanagementsystem.model.request.project.UpdateProjectRequest;
import duy.personalproject.taskmanagementsystem.model.response.project.ProjectResponse;
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
