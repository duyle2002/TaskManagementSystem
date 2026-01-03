package duy.personalproject.taskmanagementsystem.auth.controller;

import duy.personalproject.taskmanagementsystem.auth.model.request.LoginRequest;
import duy.personalproject.taskmanagementsystem.auth.model.request.RefreshTokenRequest;
import duy.personalproject.taskmanagementsystem.auth.model.request.RegisterAccountRequest;
import duy.personalproject.taskmanagementsystem.auth.model.response.LoginResponse;
import duy.personalproject.taskmanagementsystem.core.annotation.LogExecutionTime;
import duy.personalproject.taskmanagementsystem.core.model.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import duy.personalproject.taskmanagementsystem.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication and registration")
@Slf4j(topic = "AUTH_CONTROLLER")
@LogExecutionTime
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Register a new user account",
            description = "Creates a new user account with the provided registration details."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Account registered successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "User already exists with the provided email or username"
            )
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerAccount(@Valid @RequestBody RegisterAccountRequest registerAccountRequest) {
        log.info("Received request to register account for user {}", registerAccountRequest.getUsername());
        authService.registerAccount(registerAccountRequest);
        return ResponseEntity.ok(
                ApiResponse.okWithMessage("User registered successfully")
        );
    }

    @Operation(
            summary = "User login",
            description = "Authenticates a user and returns a JWT token upon successful login."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Login successful"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid username or password"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Received request to login for user {}", loginRequest.getUsername());
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.ok(loginResponse));
    }

    @Operation(
            summary = "Refresh JWT token",
            description = "Generates a new JWT token using a valid refresh token."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid or expired refresh token"
            )
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("Received request to refresh token");
        LoginResponse loginResponse = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(ApiResponse.ok(loginResponse));
    }
}
