package duy.personalproject.taskmanagementsystem.model.common;

import duy.personalproject.taskmanagementsystem.model.constant.PaginationConstants;
import duy.personalproject.taskmanagementsystem.model.enums.SortDirection;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {
    @Parameter(description = "Page number for pagination (0-based)", example = "0")
    @Schema(defaultValue = "0")
    private Integer page;

    @Parameter(description = "Number of items per page for pagination", example = "10")
    @Schema(defaultValue = "10")
    private Integer size;

    @Parameter(description = "Field to sort by", example = "createdAt")
    @Schema(defaultValue = "createdAt")
    private String sortBy;

    @Parameter(description = "Sort direction: ASC or DESC", example = "DESC")
    @Schema(defaultValue = "DESC")
    private SortDirection sortDirection;

    public void applyDefaults() {
        if (this.page == null || this.page < 0) {
            this.page = PaginationConstants.DEFAULT_PAGE;
        }
        if (this.size == null || this.size <= 0) {
            this.size = PaginationConstants.DEFAULT_SIZE;
        }
        if (this.sortBy == null || this.sortBy.isBlank()) {
            this.sortBy = PaginationConstants.SORT_BY_CREATED_AT;
        }
        if (this.sortDirection == null) {
            this.sortDirection = SortDirection.DESC;
        }
    }
}
