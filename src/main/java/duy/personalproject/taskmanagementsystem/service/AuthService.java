package duy.personalproject.taskmanagementsystem.service;

import duy.personalproject.taskmanagementsystem.model.request.auth.LoginRequest;
import duy.personalproject.taskmanagementsystem.model.request.auth.RegisterAccountRequest;
import duy.personalproject.taskmanagementsystem.model.response.auth.LoginResponse;

public interface AuthService {
    void registerAccount(RegisterAccountRequest registerAccountRequest);
    LoginResponse login(LoginRequest loginRequest);
}
