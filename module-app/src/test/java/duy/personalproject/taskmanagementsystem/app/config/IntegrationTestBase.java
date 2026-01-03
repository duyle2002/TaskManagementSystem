package duy.personalproject.taskmanagementsystem.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import duy.personalproject.taskmanagementsystem.auth.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.auth.repository.UserRepository;
import duy.personalproject.taskmanagementsystem.auth.service.JwtService;
import duy.personalproject.taskmanagementsystem.core.model.enums.UserRole;
import duy.personalproject.taskmanagementsystem.core.model.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base class for integration tests with TestContainers support.
 * All integration tests should extend this class to get:
 * - PostgreSQL TestContainer
 * - Full Spring context
 * - MockMvc for API testing
 * - Common test utilities
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@Import(TestContainersConfiguration.class)
@Transactional
public abstract class IntegrationTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected JwtService jwtService;

    protected static final String DEFAULT_TEST_PASSWORD = "Test@1234";

    @BeforeEach
    void baseSetUp() {
        // Clean up data before each test
        userRepository.deleteAll();
    }

    /**
     * Helper method to create a test user
     *
     * @param username the username
     * @param email    the email
     * @param role     the user role
     * @return the created user entity
     */
    protected UserEntity createTestUser(String username, String email, UserRole role) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(DEFAULT_TEST_PASSWORD));
        user.setFullName("Test User");
        user.setRole(role);
        user.setUserStatus(UserStatus.ACTIVE);
        return userRepository.save(user);
    }

    /**
     * Helper method to create a test user with custom password
     *
     * @param username the username
     * @param email    the email
     * @param password the password
     * @param role     the user role
     * @return the created user entity
     */
    protected UserEntity createTestUser(String username, String email, String password, UserRole role) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName("Test User");
        user.setRole(role);
        user.setUserStatus(UserStatus.ACTIVE);
        return userRepository.save(user);
    }

    /**
     * Helper method to generate JWT token for a user
     *
     * @param user the user entity
     * @return the JWT token string
     */
    protected String generateToken(UserEntity user) {
        return jwtService.generateAccessToken(user).token();
    }
}

