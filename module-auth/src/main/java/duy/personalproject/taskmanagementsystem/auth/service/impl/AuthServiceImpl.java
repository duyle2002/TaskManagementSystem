package duy.personalproject.taskmanagementsystem.auth.service.impl;

import duy.personalproject.taskmanagementsystem.auth.model.request.LoginRequest;
import duy.personalproject.taskmanagementsystem.auth.model.request.RefreshTokenRequest;
import duy.personalproject.taskmanagementsystem.auth.model.request.RegisterAccountRequest;
import duy.personalproject.taskmanagementsystem.auth.model.response.LoginResponse;
import duy.personalproject.taskmanagementsystem.auth.model.response.TokenInfo;
import duy.personalproject.taskmanagementsystem.core.exception.DuplicateResourceException;
import duy.personalproject.taskmanagementsystem.auth.mapper.UserMapper;
import duy.personalproject.taskmanagementsystem.auth.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.auth.repository.UserRepository;
import duy.personalproject.taskmanagementsystem.auth.security.CustomUserDetails;
import duy.personalproject.taskmanagementsystem.auth.service.AuthService;
import duy.personalproject.taskmanagementsystem.auth.service.JwtService;
import duy.personalproject.taskmanagementsystem.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTH_SERVICE")
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    /**
     * Register a new user account.
     *
     * @param registerAccountRequest the registration request containing user details
     * @throws DuplicateResourceException the email or username already exists
     */
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
        userRepository.save(userEntity);
    }

    /**
     * Login a user and generate JWT tokens.
     *
     * @param loginRequest the login request containing username and password
     * @return LoginResponse containing access and refresh tokens
     */
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

        TokenInfo accessToken = jwtService.generateAccessToken(userEntity);
        TokenInfo refreshToken = refreshTokenService.createRefreshToken(userEntity);

        return LoginResponse.builder()
                .accessToken(accessToken.token())
                .refreshToken(refreshToken.token())
                .expiresAt(accessToken.expiresAt())
                .build();
    }

    /**
     * Refresh JWT tokens using a valid refresh token.
     *
     * Generates a new access token and refresh token, invalidating the old refresh token.
     *
     * @param refreshTokenRequest the request containing the refresh token to validate
     * @return LoginResponse containing new access and refresh tokens
     */
    @Override
    public LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.refreshToken();
        UserEntity userEntity = refreshTokenService.validateAndRetrieveUser(refreshToken);

        // Invalidate the old refresh token
        refreshTokenService.revokeRefreshToken(refreshTokenRequest.refreshToken());

        TokenInfo accessToken = jwtService.generateAccessToken(userEntity);
        TokenInfo refreshTokenInfo = refreshTokenService.createRefreshToken(userEntity);

        return LoginResponse.builder()
                .accessToken(accessToken.token())
                .refreshToken(refreshTokenInfo.token())
                .expiresAt(accessToken.expiresAt())
                .build();
    }
}
