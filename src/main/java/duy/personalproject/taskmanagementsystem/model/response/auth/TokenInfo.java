package duy.personalproject.taskmanagementsystem.model.response.auth;

public record TokenInfo(
        String token,
        long expiresAt
){}