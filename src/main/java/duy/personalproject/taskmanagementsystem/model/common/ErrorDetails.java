package duy.personalproject.taskmanagementsystem.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Error details in API response.
 * Contains error code, detailed message, and field validation errors.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {
    /**
     * Error code for programmatic handling.
     */
    private String code;

    /**
     * Detailed error message.
     */
    private String details;

    /**
     * Field validation errors (key: field name, value: error message).
     */
    private Map<String, String> fieldErrors;

    /**
     * Create error details with code and message.
     */
    public static ErrorDetails of(String code, String details) {
        return ErrorDetails.builder()
                .code(code)
                .details(details)
                .build();
    }

    /**
     * Create error details with field errors.
     */
    public static ErrorDetails withFieldErrors(String code, String details, Map<String, String> fieldErrors) {
        return ErrorDetails.builder()
                .code(code)
                .details(details)
                .fieldErrors(fieldErrors)
                .build();
    }
}

