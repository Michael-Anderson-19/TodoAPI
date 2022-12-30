package io.github.MichaelAnderson19.TodoAPI.service;

import io.github.MichaelAnderson19.TodoAPI.dto.UserDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.DeleteUserRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.RegistrationRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.PasswordChangeRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.UpdateUserRequestDto;
import io.github.MichaelAnderson19.TodoAPI.exception.InvalidCredentialsException;
import io.github.MichaelAnderson19.TodoAPI.exception.UserAlreadyExistsException;
import io.github.MichaelAnderson19.TodoAPI.exception.UserNotFoundException;
import io.github.MichaelAnderson19.TodoAPI.model.User;
import io.github.MichaelAnderson19.TodoAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    //create user
    public User createUser(RegistrationRequestDto registrationDto) {

        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(String.format("Exception: An account with the email address %s already exists", registrationDto.getEmail()));
        }

        return userRepository.save(
                User.builder()
                        .email(registrationDto.getEmail())
                        .password(encoder.encode(registrationDto.getPassword()))
                        .username(registrationDto.getUsername()) //username should also be unique
                        .roles("USER")
                        .build()
        );
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
    public User updateUserDetails(String principalEmail, UpdateUserRequestDto updateUserDto) {
        User user = userRepository.findByEmail(updateUserDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(String.format("user with email %s not found", principalEmail)));
        if (!verifyPassword(updateUserDto.getPassword(), principalEmail)) {
            throw new InvalidCredentialsException("Error: password does not match");
        }
        user.setEmail(updateUserDto.getEmail());
        user.setUsername(updateUserDto.getUsername());
        //first and last name????
        return userRepository.save(user);
    }

    //delete user
    public void deleteUser(DeleteUserRequestDto deleteUserDto, String principalEmail) { //may invert ifs
        if (!deleteUserDto.getEmail().equals(principalEmail)) {
            throw new InvalidCredentialsException("Error: email does not match");
        }
        User user = userRepository.findByEmail(deleteUserDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(String.format("user with email %s not found", principalEmail)));

        if (!encoder.matches(deleteUserDto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Error: password does not match");
        }
        userRepository.delete(user);
    }
    //get user
    public UserDto getUser(String principalEmail) {
        User user = userRepository.findByEmail(principalEmail)
                .orElseThrow(() -> new UserNotFoundException(String.format("user with email %s not found", principalEmail)));
        return UserDto.builder().email(user.getEmail()).username(user.getUsername()).build();
    }

    private boolean verifyPassword(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
