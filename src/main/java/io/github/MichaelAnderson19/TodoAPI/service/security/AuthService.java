package io.github.MichaelAnderson19.TodoAPI.service.security;

import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.LoginRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.RefreshTokenRequest;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.response.JwtResponse;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.response.TokenRefreshResponse;

public interface AuthService {
    JwtResponse login(LoginRequestDto loginRequest);

    void logoutUser();

    TokenRefreshResponse refreshJwtToken(RefreshTokenRequest refreshRequest);
}
