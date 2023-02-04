package io.github.MichaelAnderson19.TodoAPI.service.security;

import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.LoginRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.response.JwtResponse;

public interface AuthService {
    JwtResponse login(LoginRequestDto loginRequest);
}
