package io.github.MichaelAnderson19.TodoAPI.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationDto {

    private String email;
    private String password;
    private String username;
    private String roles;
}
