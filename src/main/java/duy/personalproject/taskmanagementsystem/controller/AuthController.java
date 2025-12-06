package duy.personalproject.taskmanagementsystem.controller;

import duy.personalproject.taskmanagementsystem.model.common.ApiResponse;
import duy.personalproject.taskmanagementsystem.model.request.auth.RegisterAccountRequest;
import duy.personalproject.taskmanagementsystem.service.auth.IAuthService;
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
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerAccount(@Valid @RequestBody RegisterAccountRequest registerAccountRequest) {
        log.info("Received request to register account for user {}", registerAccountRequest.getUsername());
        authService.registerAccount(registerAccountRequest);
        return ResponseEntity.ok(
                ApiResponse.okWithMessage("Account registered successfully")
        );
    }
}
