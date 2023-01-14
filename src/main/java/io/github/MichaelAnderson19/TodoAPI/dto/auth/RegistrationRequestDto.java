package io.github.MichaelAnderson19.TodoAPI.dto.auth;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequestDto {

    @NotEmpty(message="Email must not be empty")
    @Email(message = "Email must be valid")
    private String email;

    @NotEmpty(message = "Password must not be empty")
    @Size(min=8, message="Password must be a minimum of 8 characters")
    private String password;

    @NotEmpty(message = "Username must not be empty")
    private String username;

    private String roles;
}
