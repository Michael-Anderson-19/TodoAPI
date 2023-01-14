package io.github.MichaelAnderson19.TodoAPI.service.security;

import io.github.MichaelAnderson19.TodoAPI.configuration.security.JwtUtils;
import io.github.MichaelAnderson19.TodoAPI.service.security.impl.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.AuthenticationManager;

import static org.mockito.Mockito.*;


public class AuthServiceTest {

    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImpl userDetailsService;
    private AuthService authService;

    @BeforeEach
    public void setUp(){
        jwtUtils = mock(JwtUtils.class);
        authenticationManager = mock(AuthenticationManager.class);
        userDetailsService = mock(UserDetailsServiceImpl.class);
        authService = new AuthServiceImpl(userDetailsService,authenticationManager,jwtUtils);
    }

    //login happy path
    //login unhappy paht - wrong credentials


}
