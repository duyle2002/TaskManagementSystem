package duy.personalproject.taskmanagementsystem.integration.controller;

import duy.personalproject.taskmanagementsystem.config.IntegrationTestBase;
import duy.personalproject.taskmanagementsystem.model.enums.UserRole;
import duy.personalproject.taskmanagementsystem.model.request.auth.LoginRequest;
import duy.personalproject.taskmanagementsystem.model.request.auth.RegisterAccountRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Auth Controller Integration Tests")
class AuthControllerIntegrationTest extends IntegrationTestBase {
    private static final String AUTH_BASE_URL = "/api/v1/auth";
    private static final String REGISTER_ENDPOINT = AUTH_BASE_URL + "/register";
    private static final String LOGIN_ENDPOINT = AUTH_BASE_URL + "/login";

    @Test
    @DisplayName("POST /api/v1/auth/register - Should register user successfully")
    void register_ValidRequest_ReturnsSuccess() throws Exception {
        RegisterAccountRequest request = RegisterAccountRequest.builder()
                .username("newuser123")
                .password("Test@1234")
                .email("newuser@example.com")
                .fullName("New User")
                .build();

        mockMvc.perform(post(REGISTER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    @DisplayName("POST /api/v1/auth/register - Should fail with duplicate email")
    void register_DuplicateEmail_ReturnsConflict() throws Exception {
        createTestUser("existinguser", "existing@example.com", UserRole.ROLE_USER);

        RegisterAccountRequest request = RegisterAccountRequest.builder()
                .username("newuser123")
                .password("Test@1234")
                .email("existing@example.com")
                .fullName("New User")
                .build();

        mockMvc.perform(post(REGISTER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.message").value(containsString("already exists")));
    }

    @Test
    @DisplayName("POST /api/v1/auth/register - Should fail with invalid password")
    void register_InvalidPassword_ReturnsBadRequest() throws Exception {
        RegisterAccountRequest request = RegisterAccountRequest.builder()
                .username("newuser123")
                .password("weak")
                .email("newuser@example.com")
                .fullName("New User")
                .build();

        mockMvc.perform(post(REGISTER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400));
    }


    @Test
    @DisplayName("POST /api/v1/auth/register - Should fail with blank username")
    void register_BlankUsername_ReturnsBadRequest() throws Exception {
        RegisterAccountRequest request = RegisterAccountRequest.builder()
                .username("")
                .password("Test@1234")
                .email("test@example.com")
                .fullName("Test User")
                .build();

        mockMvc.perform(post(REGISTER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("POST /api/v1/auth/register - Should fail with invalid email format")
    void register_InvalidEmail_ReturnsBadRequest() throws Exception {
        RegisterAccountRequest request = RegisterAccountRequest.builder()
                .username("testuser123")
                .password("Test@1234")
                .email("invalid-email")
                .fullName("Test User")
                .build();

        mockMvc.perform(post(REGISTER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.fieldErrors.email").exists());
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - Should login successfully")
    void login_ValidCredentials_ReturnsTokens() throws Exception {
        createTestUser("testuser", "test@example.com", UserRole.ROLE_USER);

        LoginRequest request = LoginRequest.builder()
                .username("testuser")
                .password(DEFAULT_TEST_PASSWORD)
                .build();

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.expiresAt").isNumber());
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - Should fail with invalid credentials")
    void login_InvalidCredentials_ReturnsUnauthorized() throws Exception {
        createTestUser("testuser", "test@example.com", UserRole.ROLE_USER);

        LoginRequest request = LoginRequest.builder()
                .username("testuser")
                .password("WrongPassword@123")
                .build();

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(401));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - Should fail with non-existent user")
    void login_NonExistentUser_ReturnsUnauthorized() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .username("nonexistentuser")
                .password("Test@1234")
                .build();

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }
}

