package io.github.MichaelAnderson19.TodoAPI.service;

import io.github.MichaelAnderson19.TodoAPI.dto.response.UserDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.DeleteUserRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.PasswordChangeRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.RegistrationRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.UpdateUserRequestDto;
import io.github.MichaelAnderson19.TodoAPI.exception.InvalidCredentialsException;
import io.github.MichaelAnderson19.TodoAPI.exception.UserAlreadyExistsException;
import io.github.MichaelAnderson19.TodoAPI.exception.UserNotFoundException;
import io.github.MichaelAnderson19.TodoAPI.model.User;
import io.github.MichaelAnderson19.TodoAPI.repository.UserRepository;
import io.github.MichaelAnderson19.TodoAPI.service.impl.UserServiceImpl;
import io.github.MichaelAnderson19.TodoAPI.shared.UserRole;
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

    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        passwordEncoder = mock(PasswordEncoder.class);
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("When creating a new user, that does not already exist within the database, return a UserDto")
    public void happyPathCreateUserTest() {
        String encodedPassword = "ENCODED_PASSWORD_12345";
        RegistrationRequestDto dto = new RegistrationRequestDto("test@test.com", "password", "test", "USER");
        User user = User.builder().id(null).email(dto.getEmail()).password(encodedPassword).username(dto.getUsername()).role(UserRole.USER).build();
        User savedUser = User.builder().id(1l).email(dto.getEmail()).password(encodedPassword).username(dto.getUsername()).role(UserRole.USER).build();
        when(passwordEncoder.encode(dto.getPassword())).thenReturn(encodedPassword);
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.<User>empty());
        when(userRepository.save(user)).thenReturn(savedUser);

        UserDto result = userService.createUser(dto);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(result.getUsername()).isEqualTo(savedUser.getUsername());

        verify(passwordEncoder, times(1)).encode(dto.getPassword());
        verify(userRepository, times(1)).findByEmail(dto.getEmail());
        verify(userRepository, times(1)).save(user);

    }

    @Test
    @DisplayName("When creating a new user, that already exist within the database, return that new user")
    public void unhappyPathCreateUserTest() {
        RegistrationRequestDto dto = new RegistrationRequestDto("test@test.com", "password", "test", "USER");
        User user = User.builder().id(null).email(dto.getEmail()).password(dto.getPassword()).username(dto.getUsername()).role(UserRole.USER).build();
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.createUser(dto))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining(dto.getEmail());

        verify(userRepository, times(1)).findByEmail(dto.getEmail());

    }

    ///changePassword
    @Test
    @DisplayName("When changing a user password with an existing user and correct password and email return that user with an new password")
    public void happyPathChangePassword() {
        String principalEmail = "test@test.com";
        String oldPassword = "password";
        String newPassword = "newPassword";

        PasswordChangeRequestDto passwordChangeDto = PasswordChangeRequestDto
                .builder().email(principalEmail).oldPassword(oldPassword).newPassword(newPassword).build();

        User user = User.builder().id(1L).email(principalEmail).username("test")
                .password(oldPassword).role(UserRole.USER).build();

        when(userRepository.findByEmail(principalEmail)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(passwordChangeDto.getOldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(passwordChangeDto.getNewPassword())).thenReturn(newPassword);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.changePassword(principalEmail, passwordChangeDto);
        assertThat(result.getPassword()).isEqualTo(newPassword);

        verify(passwordEncoder, times(1)).encode(passwordChangeDto.getNewPassword());
        verify(passwordEncoder, times(1)).matches(passwordChangeDto.getOldPassword(), oldPassword);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("When changing a user password with emails that do not match then throw an InvalidCredentialsException")
    public void unhappyPathChangePassword_EmailsDontMatch() {
        String principalEmail = "test@test.com";
        PasswordChangeRequestDto dto = PasswordChangeRequestDto.builder()
                .email("differentEmail@test.com")
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();

        assertThatThrownBy(() -> userService.changePassword(principalEmail, dto))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("email does not match");
    }

    @Test
    @DisplayName("When changing a user password a user that does not exist in the database then throw a UserNotFoundException")
    public void unhappyPathChangePassword_UserNotFound() {
        String principalEmail = "test@test.com";

        PasswordChangeRequestDto dto = PasswordChangeRequestDto.builder()
                .email(principalEmail)
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();

        when(userRepository.findByEmail(principalEmail)).thenReturn(Optional.<User>empty());

        assertThatThrownBy(() -> userService.changePassword(principalEmail, dto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(principalEmail);
        verify(userRepository, times(1)).findByEmail(principalEmail);
    }

    @Test
    @DisplayName("When changing a user password with passwords that do not match then throw an InvalidCredentialsException")
    public void unhappyPathChangePassword_PasswordsDontMatch() {
        String principalEmail = "test@test.com";

        User user = User.builder().id(1L).email(principalEmail).username("test")
                .password("differentPassword").role(UserRole.USER).build();

        PasswordChangeRequestDto dto = PasswordChangeRequestDto.builder()
                .email(principalEmail)
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();

        when(userRepository.findByEmail(principalEmail)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getOldPassword(), user.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> userService.changePassword(principalEmail, dto))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("password does not match");
        verify(userRepository, times(1)).findByEmail(principalEmail);
        verify(passwordEncoder, times(1)).matches(dto.getOldPassword(), user.getPassword());
    }

    //    delete user
    @Test
    @DisplayName("When deleting a user that exists in the database by their email then delete that user")
    public void happyPathDeleteUser() {
        String email = "test@test.com";
        String password = "password";
        DeleteUserRequestDto dto = DeleteUserRequestDto.builder().email(email).password(password).build();
        User user = User.builder().id(1L).email(email).password(password).username("test").role(UserRole.USER).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword())).thenReturn(true);

        userService.deleteUser(email, dto);

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, password);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("When deleting a user that does not exist in the database by their email then throw a UseNotFoundException")
    public void unhappyPathDeleteUser_userDoesNotExist() {
        String email = "test@test.com";
        String password = "password";
        DeleteUserRequestDto dto = DeleteUserRequestDto.builder().email(email).password(password).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.<User>empty());

        assertThatThrownBy(() -> userService.deleteUser(email, dto)).isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(email);

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("When deleting a user that does exist in the database but given the wrong email then throw a InvalidCredentialsException")
    public void unhappyPathDeleteUser_wrongEmail() {
        String email = "test@test.com";
        String otherEmail = "test2@test.com";
        String password = "password";
        DeleteUserRequestDto dto = DeleteUserRequestDto.builder().email(otherEmail).password(password).build();

        assertThatThrownBy(() -> userService.deleteUser(email, dto)).isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("email");
    }

    @Test
    @DisplayName("When deleting a user that does exist in the database but given the wrong password then throw a InvalidCredentialsException")
    public void unhappyPathDeleteUser_wrongPassword() {
        String email = "test@test.com";
        String password = "password";
        String password2 = "otherPassword";
        DeleteUserRequestDto dto = DeleteUserRequestDto.builder().email(email).password(password).build();
        User user = User.builder().id(1L).email(email).password(password2).username("test").role(UserRole.USER).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(email, dto))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("password");

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, password2);
    }

    //    update user
    @Test
    @DisplayName("When updating a user that exists in the database then return a UserDto")
    public void happyPathUpdateUser() {
        String email = "test@test.com";
        String password = "password";
        String username = "username";
        String newUsername = "newUsername";

        UpdateUserRequestDto dto = UpdateUserRequestDto.builder().email(email).password(password).username(newUsername).build();
        User user = User.builder().id(1L).email(email).password(password).username(username).role(UserRole.USER).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword())).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);

        UserDto result = userService.updateUserDetails(email, dto);
        assertThat(result.getUsername()).isEqualTo(dto.getUsername());
        assertThat(result.getEmail()).isEqualTo(dto.getEmail());

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, password);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("When updating a user that does not exist in the database then throw a UserNotFoundException")
    public void unhappyPathUpdateUser_userDoesNotExist() {
        String email = "test@test.com";
        String password = "password";

        UpdateUserRequestDto dto = UpdateUserRequestDto.builder().email(email).password(password).username("test").build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.<User>empty());

        assertThatThrownBy(() -> userService.updateUserDetails(email, dto)).isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(email);

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("When updating a user that exists in the database with the wrong password then throw a InvalidCredentialsException")
    public void unhappyPathUpdateUser_incorrectPassword() {
        String email = "test@test.com";
        String password = "password";
        String password2 = "otherPassword";

        UpdateUserRequestDto dto = UpdateUserRequestDto.builder().email(email).password(password).username("test").build();
        User user = User.builder().id(1L).email(email).password(password2).username("test").role(UserRole.USER).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> userService.updateUserDetails(email, dto))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("password");

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, password2);
    }

    //GET USER TEST - REFACTOR TO USE @Nest
    //TODO fix
//    @Test
//    @DisplayName("When getting a user that exists in the database by their email then return UserDto")
//    public void happyPathGetUser(){
//        String principalEmail = "testemail@test.com";
//        User user = User.builder().id(1L).email(principalEmail).password("password")
//                .username("test").roles("USER").build();
//        when(userRepository.findByEmail(principalEmail)).thenReturn(Optional.of(user));
//
//       // UserDto result = userService.getUser(principalEmail);
//        assertThat(result.getEmail()).isEqualTo(principalEmail);
//        assertThat(result.getUsername()).isEqualTo(user.getUsername());
//
//        verify(userRepository,times(1)).findByEmail(principalEmail);
//    }

    @Test
    @DisplayName("When getting a user that does not exist in the database by a given email then throw a UserNotFoundException")
    public void unhappyPathGetUser() {
        String principalEmail = "testemail@test.com";

        when(userRepository.findByEmail(principalEmail)).thenReturn(Optional.<User>empty());

        assertThatThrownBy(() -> userService.getUser(principalEmail))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(principalEmail);
        verify(userRepository, times(1)).findByEmail(principalEmail);
    }


    //----------------------------------------------------------------
    //verifyPassword
    // @Test shouldn't test private methods
    @DisplayName("when calling verifyPassword on a password and a password hash that match then return true")
    public void happyPathVerifyPassword() {

        String rawPassword = "password";
        String encodedPassword = "$2a$10$V.7w2RA0RdNLtaEoRsB38eKlhBOsszrEWh9dGpkzwqGTTJRvJgjzO";

        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        // userService.
        //assertThat()
    }

    //@Test
    @DisplayName("when calling verifyPassword on a password and a password hash that do not match then return false")
    public void unhappyPathVerifyPassword() {
        String principalEmail = "testemail@test.com";

        when(userRepository.findByEmail(principalEmail)).thenReturn(Optional.<User>empty());

        assertThatThrownBy(() -> userService.getUser(principalEmail))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(principalEmail);
        verify(userRepository, times(1)).findByEmail(principalEmail);
    }


}
