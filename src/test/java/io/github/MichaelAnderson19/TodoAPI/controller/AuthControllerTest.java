package io.github.MichaelAnderson19.TodoAPI.controller;

import io.github.MichaelAnderson19.TodoAPI.service.security.AuthService;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

public class AuthControllerTest {
    private AuthService authService;
    private AuthController authcontroller;
    @BeforeEach
    public void setUp(){
        authService = mock(AuthService.class);
        this.authcontroller = new AuthController(authService);
    }

    public void happyPathLoginUser(){}
    public void unhappyPathLoginUser(){}

    public void happyPathChangePassword(){}
    public void unhappyPathChangePassword(){}

    //integrationTests
}
