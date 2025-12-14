package duy.personalproject.taskmanagementsystem.controller;

import duy.personalproject.taskmanagementsystem.model.common.ApiResponse;
import duy.personalproject.taskmanagementsystem.model.request.auth.LoginRequest;
import duy.personalproject.taskmanagementsystem.model.request.auth.RegisterAccountRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import duy.personalproject.taskmanagementsystem.model.response.auth.LoginResponse;
import duy.personalproject.taskmanagementsystem.service.AuthService;
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
    @PostMapping("/registerAccount")
    public ResponseEntity<ApiResponse<Void>> registerAccount(@Valid @RequestBody RegisterAccountRequest registerAccountRequest) {
        log.info("Received request to register account for user {}", registerAccountRequest.getUsername());
        authService.registerAccount(registerAccountRequest);
        return ResponseEntity.ok(
                ApiResponse.okWithMessage("Account registered successfully")
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
}
