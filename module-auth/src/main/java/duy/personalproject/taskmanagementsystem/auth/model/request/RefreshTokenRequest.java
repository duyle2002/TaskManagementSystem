package duy.personalproject.taskmanagementsystem.auth.model.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
    @NotBlank
    String refreshToken
) {}
