package duy.personalproject.taskmanagementsystem.controller;

import duy.personalproject.taskmanagementsystem.model.common.ApiResponse;
import duy.personalproject.taskmanagementsystem.model.request.auth.RegisterAccountRequest;
import duy.personalproject.taskmanagementsystem.service.auth.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
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
@Slf4j
@Tag(name = "Authentication", description = "APIs for user authentication and registration")
public class AuthController {
    private final IAuthService authService;

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
                ApiResponse.okWithMessage("Account registered successfully")
        );
    }
}
