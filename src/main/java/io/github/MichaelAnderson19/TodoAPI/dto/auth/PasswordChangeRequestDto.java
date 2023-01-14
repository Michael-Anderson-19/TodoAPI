package io.github.MichaelAnderson19.TodoAPI.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordChangeRequestDto {

    @NotEmpty(message="Email must not be empty")
    @Email(message = "Email must be valid")
    private String email;

    @NotEmpty(message = "Old password must not be empty")
    private String oldPassword;

    @NotEmpty(message = "New Password must not be empty")
    @Size(min=8, message="New password must be a minimum of 8 characters")
    private String newPassword;

}
