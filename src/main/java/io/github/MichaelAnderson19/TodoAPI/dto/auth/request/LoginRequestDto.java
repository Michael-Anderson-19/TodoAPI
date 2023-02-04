package io.github.MichaelAnderson19.TodoAPI.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginRequestDto {

    @NotEmpty(message="Email must not be empty")
    @Email(message = "Email must be valid")
    private String email;

    @NotEmpty(message = "Password must not be empty")
    private String password;

}
