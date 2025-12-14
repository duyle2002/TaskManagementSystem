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
import duy.personalproject.taskmanagementsystem.security.CustomUserDetails;
import duy.personalproject.taskmanagementsystem.service.AuthService;
import duy.personalproject.taskmanagementsystem.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final AuthenticationManager authenticationManager;

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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        UserEntity userEntity = customUserDetails.getUserEntity();

        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(Instant.now().getEpochSecond())
                .build();
    }
}
