package io.github.MichaelAnderson19.TodoAPI.service.security;

import io.github.MichaelAnderson19.TodoAPI.configuration.security.JwtUtils;
import io.github.MichaelAnderson19.TodoAPI.dto.UserDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.DeleteUserRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.PasswordChangeRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.RegistrationRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.UpdateUserRequestDto;
import io.github.MichaelAnderson19.TodoAPI.exception.InvalidCredentialsException;
import io.github.MichaelAnderson19.TodoAPI.exception.UserAlreadyExistsException;
import io.github.MichaelAnderson19.TodoAPI.exception.UserNotFoundException;
import io.github.MichaelAnderson19.TodoAPI.model.User;
import io.github.MichaelAnderson19.TodoAPI.repository.UserRepository;
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
        authService = new AuthService(userDetailsService,authenticationManager,jwtUtils);
    }

    //login happy path
    //login unhappy paht - wrong credentials


}
