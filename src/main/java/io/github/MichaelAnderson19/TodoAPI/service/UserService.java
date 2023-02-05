package io.github.MichaelAnderson19.TodoAPI.service;

import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.DeleteUserRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.PasswordChangeRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.RegistrationRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.UpdateUserRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.response.UserDto;
import io.github.MichaelAnderson19.TodoAPI.model.User;

public interface UserService {
    //create user
    UserDto createUser(RegistrationRequestDto registrationDto);

    //change password
    User changePassword(String principalEmail, PasswordChangeRequestDto passwordChangeDto);

    //update user
    UserDto updateUserDetails(String principalEmail, UpdateUserRequestDto updateUserDto);

    //delete user
    void deleteUser(String principalEmail, DeleteUserRequestDto deleteUserDto);

    //get user
    User getUser(String email);

    UserDto getUserDto(String email);
}
