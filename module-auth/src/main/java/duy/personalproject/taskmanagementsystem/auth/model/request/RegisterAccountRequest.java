package duy.personalproject.taskmanagementsystem.auth.model.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class RegisterAccountRequest {
    @NotBlank
    @NotNull
    @Length(min = 6, message = "Username must be at least 6 characters")
    private String username;

    @NotBlank
    @NotNull
    @Length(min = 8)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
             message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;

    @Email(message = "Invalid email format")
    @NotNull
    private String email;

    @NotBlank
    private String fullName;
}
