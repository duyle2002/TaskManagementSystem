package duy.personalproject.taskmanagementsystem.service.auth;

import duy.personalproject.taskmanagementsystem.exception.DuplicateResourceException;
import duy.personalproject.taskmanagementsystem.mapper.IUserMapper;
import duy.personalproject.taskmanagementsystem.model.entity.UserEntity;
import duy.personalproject.taskmanagementsystem.model.enums.Role;
import duy.personalproject.taskmanagementsystem.model.request.auth.RegisterAccountRequest;
import duy.personalproject.taskmanagementsystem.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements IAuthService {
    private final IUserRepository userRepository;
    private final IUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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
        userEntity.setRole(Role.USER);
        userEntity.setPassword(passwordEncoder.encode(registerAccountRequest.getPassword()));
        userRepository.save(userEntity);
    }
}
