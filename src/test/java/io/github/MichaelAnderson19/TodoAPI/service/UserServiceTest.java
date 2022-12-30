package io.github.MichaelAnderson19.TodoAPI.service;

import io.github.MichaelAnderson19.TodoAPI.dto.auth.RegistrationRequestDto;
import io.github.MichaelAnderson19.TodoAPI.exception.UserAlreadyExistsException;
import io.github.MichaelAnderson19.TodoAPI.model.User;
import io.github.MichaelAnderson19.TodoAPI.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.*;
import java.util.Optional;

public class UserServiceTest {

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setUp(){
        passwordEncoder = mock(PasswordEncoder.class);
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository,passwordEncoder);
    }

    @Test
    @DisplayName("When creating a new user, that does not already exist within the database, return that new user")
    public void happyPathCreateUserTest(){
        String encodedPassword = "ENCODED_PASSWORD_12345";
        RegistrationRequestDto dto = new RegistrationRequestDto("test@test.com", "password", "test","USER");
        User user = User.builder().id(null).email(dto.getEmail()).password(encodedPassword).username(dto.getUsername()).roles(dto.getRoles()).build();
        User savedUser = User.builder().id(1l).email(dto.getEmail()).password(encodedPassword).username(dto.getUsername()).roles(dto.getRoles()).build();
        when(passwordEncoder.encode(dto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.<User>empty());
        when(userRepository.save(user)).thenReturn(savedUser);

        User result = userService.createUser(dto);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(savedUser);

        verify(passwordEncoder,times(1)).encode(dto.getPassword());
        verify(userRepository,times(1)).findByEmail(dto.getEmail());
        verify(userRepository,times(1)).save(user);

    }

    @Test
    @DisplayName("When creating a new user, that already exist within the database, return that new user")
    public void unhappyPathCreateUserTest(){
        RegistrationRequestDto dto = new RegistrationRequestDto("test@test.com", "password", "test","USER");
        User user = User.builder().id(null).email(dto.getEmail()).password(dto.getPassword()).username(dto.getUsername()).roles(dto.getRoles()).build();
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));

        assertThatThrownBy( ()-> userService.createUser(dto) )
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining(dto.getEmail());

        verify(userRepository,times(1)).findByEmail(dto.getEmail());

    }
}
