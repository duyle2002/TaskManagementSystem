package duy.personalproject.taskmanagementsystem.service.auth;

import duy.personalproject.taskmanagementsystem.model.request.auth.RegisterAccountRequest;

public interface IAuthService {
    void registerAccount(RegisterAccountRequest registerAccountRequest);
}
