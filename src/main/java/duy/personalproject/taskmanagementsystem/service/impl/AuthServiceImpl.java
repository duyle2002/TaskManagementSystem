package duy.personalproject.taskmanagementsystem.service.impl;

import duy.personalproject.taskmanagementsystem.exception.DuplicateResourceException;
import duy.personalproject.taskmanagementsystem.exception.UnauthorizedException;
import duy.personalproject.taskmanagementsystem.mapper.IUserMapper;
import duy.personalproject.taskmanagementsystem.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.model.enums.UserRole;
import duy.personalproject.taskmanagementsystem.model.enums.UserStatus;
import duy.personalproject.taskmanagementsystem.model.request.auth.LoginRequest;
import duy.personalproject.taskmanagementsystem.model.request.auth.RegisterAccountRequest;
import duy.personalproject.taskmanagementsystem.model.response.auth.LoginResponse;
import duy.personalproject.taskmanagementsystem.repository.IUserRepository;
import duy.personalproject.taskmanagementsystem.service.AuthService;
import duy.personalproject.taskmanagementsystem.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTH_SERVICE")
public class AuthServiceImpl implements AuthService {
    private final IUserRepository userRepository;
    private final IUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public void registerAccount(RegisterAccountRequest registerAccountRequest) {
        boolean existsByEmail = userRepository.existsByEmail(registerAccountRequest.getEmail());
        if (existsByEmail) {
            log.error("User with email {} already exists", registerAccountRequest.getEmail());
            throw new DuplicateResourceException("User already exists with this email");
        }

        boolean existsByUsername = userRepository.existsByUsername(registerAccountRequest.getUsername());
        if (existsByUsername) {
            log.error("User with username {} already exists", registerAccountRequest.getUsername());
            throw new DuplicateResourceException("User already exists with this username");
        }

        UserEntity userEntity = userMapper.toUserEntity(registerAccountRequest);
        userEntity.setPassword(passwordEncoder.encode(registerAccountRequest.getPassword()));
        userEntity.setRole(UserRole.ROLE_USER);
        userEntity.setUserStatus(UserStatus.ACTIVE);
        userRepository.save(userEntity);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity userEntity = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> {
                    log.error("User with username {} not found", loginRequest.getUsername());
                    return new UnauthorizedException("Invalid username or password");
                });

        boolean isPasswordValid = checkPassword(loginRequest.getPassword(), userEntity.getPassword());

        if (!isPasswordValid) {
            log.error("Invalid password for user {}", loginRequest.getUsername());
            throw new UnauthorizedException("Invalid username or password");
        }

        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(Instant.now().getEpochSecond())
                .build();
    }

    private boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
