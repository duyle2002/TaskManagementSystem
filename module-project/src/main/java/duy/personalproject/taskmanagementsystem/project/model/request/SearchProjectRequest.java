package duy.personalproject.taskmanagementsystem.project.model.request;

import duy.personalproject.taskmanagementsystem.core.model.constant.PaginationConstants;
import duy.personalproject.taskmanagementsystem.core.model.enums.SortDirection;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request model for searching projects with pagination and sorting")
public record SearchProjectRequest(
        @Parameter(description = "Page number for pagination (0-based index)", example = "0")
        @Schema(defaultValue = "0", minimum = "0")
        Integer page,

        @Parameter(description = "Number of items per page for pagination", example = "10")
        @Schema(defaultValue = "10", minimum = "1")
        Integer size,

        @Parameter(description = "Field to sort by", example = "createdAt")
        @Schema(defaultValue = "createdAt")
        String sortBy,

        @Parameter(description = "Direction of sorting", example = "DESC")
        @Schema(defaultValue = "DESC")
        SortDirection sortDirection,

        @Parameter(description = "Search keyword to filter projects by name or description", example = "project")
        @Schema(defaultValue = "")
        String search
) {
    public SearchProjectRequest {
        int defaultPage = PaginationConstants.DEFAULT_PAGE;
        int defaultSize = PaginationConstants.DEFAULT_SIZE;

        page = (page == null || page < 0) ? defaultPage : page;
        size = (size == null || size <= 0) ? defaultSize : size;
        sortBy = (sortBy == null || sortBy.isBlank()) ? PaginationConstants.SORT_BY_CREATED_AT : sortBy;
        sortDirection = (sortDirection == null) ? SortDirection.DESC : sortDirection;
    }
}