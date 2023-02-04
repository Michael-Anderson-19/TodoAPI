package io.github.MichaelAnderson19.TodoAPI.service.security;

import io.github.MichaelAnderson19.TodoAPI.dto.auth.LoginRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequest);
}
