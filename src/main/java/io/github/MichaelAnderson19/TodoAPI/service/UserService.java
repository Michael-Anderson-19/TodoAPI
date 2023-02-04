package io.github.MichaelAnderson19.TodoAPI.service;

import io.github.MichaelAnderson19.TodoAPI.dto.response.UserDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.DeleteUserRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.RegistrationRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.PasswordChangeRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.UpdateUserRequestDto;
import io.github.MichaelAnderson19.TodoAPI.exception.InvalidCredentialsException;
import io.github.MichaelAnderson19.TodoAPI.exception.UserAlreadyExistsException;
import io.github.MichaelAnderson19.TodoAPI.exception.UserNotFoundException;
import io.github.MichaelAnderson19.TodoAPI.model.User;
import io.github.MichaelAnderson19.TodoAPI.repository.UserRepository;
import io.github.MichaelAnderson19.TodoAPI.shared.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    //create user
    public UserDto createUser(RegistrationRequestDto registrationDto) {

        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(String.format("Exception: An account with the email address %s already exists", registrationDto.getEmail()));
        }
        
        //check for user with username if does thrwo exception

        User user = userRepository.save(
                User.builder()
                        .email(registrationDto.getEmail())
                        .password(encoder.encode(registrationDto.getPassword()))
                        .username(registrationDto.getUsername()) //username should also be unique
                        .role(UserRole.USER)
                        .build()
        );

        return UserDto.builder().email(user.getEmail()).username(user.getUsername()).build();
    }

    //change password
    public User changePassword(String principalEmail, PasswordChangeRequestDto passwordChangeDto) {
        if (!principalEmail.equalsIgnoreCase(passwordChangeDto.getEmail())) {
            throw new InvalidCredentialsException("Error: email does not match");
        }

        User user = userRepository.findByEmail(principalEmail)
                .orElseThrow(() -> new UserNotFoundException(String.format("user with email %s not found", principalEmail)));

        if (!encoder.matches(passwordChangeDto.getOldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Error: password does not match");
        }

        user.setPassword(encoder.encode(passwordChangeDto.getNewPassword()));
        return userRepository.save(user);
    }

    //update user
    public UserDto updateUserDetails(String principalEmail, UpdateUserRequestDto updateUserDto) {
        User user = userRepository.findByEmail(updateUserDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(String.format("user with email %s not found", principalEmail)));
        if (!verifyPassword(updateUserDto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Error: password does not match");
        }
        user.setEmail(updateUserDto.getEmail());
        user.setUsername(updateUserDto.getUsername());
        //first and last name????
        user = userRepository.save(user);
        return UserDto.builder().email(user.getEmail()).username(user.getUsername()).build();
    }

    //delete user
    public void deleteUser(String principalEmail, DeleteUserRequestDto deleteUserDto) { //may invert ifs
        if (!deleteUserDto.getEmail().equals(principalEmail)) {
            throw new InvalidCredentialsException("Error: email does not match");
        }
        //TODO refactor this and use getUser
        User user = userRepository.findByEmail(deleteUserDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(String.format("user with email %s not found", principalEmail)));

        if (!encoder.matches(deleteUserDto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Error: password does not match");
        }
        userRepository.delete(user);
    }

    //get user
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("user with email %s not found", email)));

    }

    public UserDto getUserDto(String email) {
        User user = getUser(email);
        return UserDto.builder().email(user.getEmail()).username(user.getUsername()).build();
    }

    //TODO refactor rest of code to use this once learnt how to spy in mockito
    private boolean verifyPassword(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
