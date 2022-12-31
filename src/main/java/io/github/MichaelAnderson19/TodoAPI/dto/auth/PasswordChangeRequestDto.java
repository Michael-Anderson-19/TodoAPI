package io.github.MichaelAnderson19.TodoAPI.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordChangeRequestDto {

    private String email;
    private String oldPassword;
    private String newPassword;

}
