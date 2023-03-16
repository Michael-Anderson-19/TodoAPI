package io.github.MichaelAnderson19.TodoAPI.service.security;

import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.LoginRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.RefreshTokenRequest;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.RegistrationRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.response.JwtResponse;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.response.TokenRefreshResponse;
import io.github.MichaelAnderson19.TodoAPI.dto.response.UserDto;

public interface AuthService {
    UserDto getCurrentUser();

    JwtResponse login(LoginRequestDto loginRequest);

    void logoutUser(); //TODO could return true / false

    void registerNewUser(RegistrationRequestDto registrationDto);

    TokenRefreshResponse refreshJwtToken(RefreshTokenRequest refreshRequest);
}
