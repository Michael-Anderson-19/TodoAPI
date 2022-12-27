package io.github.MichaelAnderson19.TodoAPI.controller;

import io.github.MichaelAnderson19.TodoAPI.dto.LoginRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.LoginResponseDto;
import io.github.MichaelAnderson19.TodoAPI.service.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(@RequestBody LoginRequestDto loginRequest) {
        return ResponseEntity.ok().body(authService.login(loginRequest));
    }




}
