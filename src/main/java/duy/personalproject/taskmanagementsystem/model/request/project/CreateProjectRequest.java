package duy.personalproject.taskmanagementsystem.model.request.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateProjectRequest(
        @NotNull
        @NotBlank
        @Size(min = 3, message = "Project name must be at least 3 characters long")
        String name,
        String description
) {
}