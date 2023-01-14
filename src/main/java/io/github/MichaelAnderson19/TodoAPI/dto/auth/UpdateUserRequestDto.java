package io.github.MichaelAnderson19.TodoAPI.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequestDto {

    @NotEmpty(message="Email must not be empty")
    @Email(message = "Email must be valid")
    private String email;

    @NotEmpty(message="Username must not be empty")
    private String username;

    @NotEmpty(message = "Password must not be empty")
    private String password;

}
