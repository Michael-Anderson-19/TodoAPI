package io.github.MichaelAnderson19.TodoAPI.dto.auth;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequestDto {

    private String email;
    private String password;
    private String username;
    private String roles;
}
