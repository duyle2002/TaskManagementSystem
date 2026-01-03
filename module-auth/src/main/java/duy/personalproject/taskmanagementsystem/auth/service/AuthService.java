package duy.personalproject.taskmanagementsystem.auth.service;

import duy.personalproject.taskmanagementsystem.auth.model.request.LoginRequest;
import duy.personalproject.taskmanagementsystem.auth.model.request.RefreshTokenRequest;
import duy.personalproject.taskmanagementsystem.auth.model.request.RegisterAccountRequest;
import duy.personalproject.taskmanagementsystem.auth.model.response.LoginResponse;

public interface AuthService {
    void registerAccount(RegisterAccountRequest registerAccountRequest);
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
