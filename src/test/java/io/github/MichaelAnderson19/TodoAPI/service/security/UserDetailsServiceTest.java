package io.github.MichaelAnderson19.TodoAPI.service.security;

import io.github.MichaelAnderson19.TodoAPI.model.User;
import io.github.MichaelAnderson19.TodoAPI.repository.UserRepository;
import io.github.MichaelAnderson19.TodoAPI.service.security.impl.UserDetailsServiceImpl;
import io.github.MichaelAnderson19.TodoAPI.shared.UserRole;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;


public class UserDetailsServiceTest {

    private UserRepository userRepository;

    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    @DisplayName("When loading a user, that exists in the database, by email then Return SecurityUser with the user details")
    public void happyPathLoadUserByUsername() {
        String email = "test@test.com";
        User user = User.builder().id(1L).username("Test").email(email).password("password").role(UserRole.USER).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername(email);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(user.getPassword());

        verify(userRepository, times(1)).findByEmail(email);
    }


    @Test
    @DisplayName("When loading a user, that does not exist in the database, by email then throw an exception")
    public void unhappyPathLoadUserByUsername() {
        String email = "test@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.<User>empty());
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining(email);
        verify(userRepository, times(1)).findByEmail(email);
    }
}
