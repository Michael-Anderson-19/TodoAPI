package io.github.MichaelAnderson19.TodoAPI.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {

    private String userEmail;

    private String jwtToken;

    private String refreshToken;

    private String role;
    //add in the response token

}
