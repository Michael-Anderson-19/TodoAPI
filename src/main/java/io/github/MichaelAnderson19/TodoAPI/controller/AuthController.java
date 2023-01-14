package io.github.MichaelAnderson19.TodoAPI.controller;

import io.github.MichaelAnderson19.TodoAPI.dto.UserDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.LoginRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.LoginResponseDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.RegistrationRequestDto;
import io.github.MichaelAnderson19.TodoAPI.service.UserService;
import io.github.MichaelAnderson19.TodoAPI.service.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginRequestDto loginRequest) {
        return ResponseEntity.ok().body(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> createNewUser(@RequestBody RegistrationRequestDto registrationDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                userService.createUser(registrationDto));
    }



}
