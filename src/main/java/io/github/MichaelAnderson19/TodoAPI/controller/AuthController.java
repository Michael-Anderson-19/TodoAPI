package io.github.MichaelAnderson19.TodoAPI.controller;

import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.RefreshTokenRequest;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.response.TokenRefreshResponse;
import io.github.MichaelAnderson19.TodoAPI.dto.response.UserDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.LoginRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.response.JwtResponse;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.RegistrationRequestDto;
import io.github.MichaelAnderson19.TodoAPI.service.UserService;
import io.github.MichaelAnderson19.TodoAPI.service.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<JwtResponse> loginUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest)); //ad creation of the refresh token in the auth service
        //evcerything should really come through this and the auth service since this is the auth controller - the service calls other services not hte controller
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> createNewUser(@Valid @RequestBody RegistrationRequestDto registrationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                userService.createUser(registrationDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshUsersJWTToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(
                authService.refreshJwtToken(refreshTokenRequest)
        );
    }


}
