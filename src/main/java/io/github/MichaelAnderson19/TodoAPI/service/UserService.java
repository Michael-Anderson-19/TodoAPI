package io.github.MichaelAnderson19.TodoAPI.service;

import io.github.MichaelAnderson19.TodoAPI.dto.RegistrationDto;
import io.github.MichaelAnderson19.TodoAPI.exception.UserAlreadyExistsException;
import io.github.MichaelAnderson19.TodoAPI.model.User;
import io.github.MichaelAnderson19.TodoAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public User createUser(RegistrationDto registrationDto) {

        if(userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(String.format("Exception: An account with the email address %s already exists",registrationDto.getEmail()));
        }

        return userRepository.save(
                User.builder()
                        .email(registrationDto.getEmail())
                        .password(encoder.encode(registrationDto.getPassword()))
                        .username(registrationDto.getUsername()) //username should also be unique
                        .build()
        );
    }

    //update user

    //delete user

    //get user

    //get all users


}
