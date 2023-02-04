package io.github.MichaelAnderson19.TodoAPI.dto.auth.request;

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
public class DeleteUserRequestDto {

    @NotEmpty(message="Email must not be empty")
    private String email;
    @NotEmpty(message="Password must not be empty")
    private String password;
}
