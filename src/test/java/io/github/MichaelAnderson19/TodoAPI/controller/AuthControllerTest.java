package io.github.MichaelAnderson19.TodoAPI.controller;

import io.github.MichaelAnderson19.TodoAPI.dto.auth.*;
import io.github.MichaelAnderson19.TodoAPI.service.UserService;
import io.github.MichaelAnderson19.TodoAPI.service.security.AuthService;
import io.github.MichaelAnderson19.TodoAPI.dto.UserDto;
import io.github.MichaelAnderson19.TodoAPI.exception.InvalidCredentialsException;
import io.github.MichaelAnderson19.TodoAPI.exception.UserAlreadyExistsException;
import io.github.MichaelAnderson19.TodoAPI.exception.UserNotFoundException;
import io.github.MichaelAnderson19.TodoAPI.model.User;
import io.github.MichaelAnderson19.TodoAPI.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static io.github.MichaelAnderson19.TodoAPI.dto.auth.LoginResponseDto.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


import static org.mockito.Mockito.mock;

public class AuthControllerTest {
    private AuthService authService;
    private UserService userService;
    private AuthController authcontroller;
    @BeforeEach
    public void setUp(){
        authService = mock(AuthService.class);
        userService = mock(UserService.class);
        authcontroller = new AuthController(authService, userService);
    }

    @Test
    @DisplayName("when logging in with a LoginRequestDto containing the username and password of a user in the database then return a loginResponseDto containing a jwt token and user email")
    public void happyPathLoginUser(){
        String email = "test@test.com";
        String password = "password";
        String token = "jwtToken";

        LoginRequestDto dtoRequest = LoginRequestDto.builder().userEmail(email).userPassword(password).build();
        LoginResponseDto dtoResponse = builder().userEmail(email).jwtToken(token).build();

        when(authService.login(dtoRequest)).thenReturn(dtoResponse);

        ResponseEntity<LoginResponseDto> result = authcontroller.loginUser(dtoRequest);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(dtoResponse);
        assertThat(result.getBody().getUserEmail()).isEqualTo(email);
        assertThat(result.getBody().getJwtToken()).isEqualTo(token);

        verify(authService,times(1)).login(dtoRequest);
    }

    @Test
    public void happyPathChangePassword(){
        String email = "test@test.com";
        String password = "password";
        String username = "test";

        RegistrationRequestDto dtoRequest = RegistrationRequestDto.builder().email(email).password(password).username(username).build();
        UserDto dtoResponse = UserDto.builder().email(email).username(username).build();

        when(userService.createUser(dtoRequest)).thenReturn(dtoResponse);

        ResponseEntity<UserDto> result = authcontroller.createNewUser(dtoRequest);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isEqualTo(dtoResponse);
        assertThat(result.getBody().getEmail()).isEqualTo(email);
        assertThat(result.getBody().getUsername()).isEqualTo(username);

        verify(userService,times(1)).createUser(dtoRequest);
    }


    //TODO integrationTests
}
