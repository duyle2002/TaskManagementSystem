package duy.personalproject.taskmanagementsystem.model.common;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Builder
public record PaginationResponse<T>(
        List<T> items,
        PageMetadata metadata
) {
    @Builder
    public record PageMetadata(
            int pageNumber,
            int pageSize,
            long totalItems,
            int totalPages,
            boolean isLastPage,
            boolean isFirstPage,
            boolean isEmpty,
            int numberOfElements
    ) {}

    /**
     * Factory method to create PaginationResponse from Spring Data Page.
     * This prevents direct serialization of PageImpl.
     *
     * @param page Spring Data Page object
     * @param <T> Type of content
     * @return PaginationResponse with all metadata
     */
    public static <T> PaginationResponse<T> of(Page<T> page) {
        PageMetadata metadata = PageMetadata.builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isFirstPage(page.isFirst())
                .isLastPage(page.isLast())
                .isEmpty(page.isEmpty())
                .numberOfElements(page.getNumberOfElements())
                .build();

        return PaginationResponse.<T>builder()
                .items(page.getContent())
                .metadata(metadata)
                .build();
    }

    /**
     * Factory method to create PaginationResponse from Spring Data Page with mapping.
     * Useful when you need to convert entities to DTOs.
     *
     * @param page Spring Data Page object
     * @param mapper Function to map from source type to target type
     * @param <S> Source type
     * @param <T> Target type
     * @return PaginationResponse with mapped content
     */
    public static <S, T> PaginationResponse<T> of(Page<S> page, Function<S, T> mapper) {
        Page<T> mappedPage = page.map(mapper);
        return of(mappedPage);
    }
}
