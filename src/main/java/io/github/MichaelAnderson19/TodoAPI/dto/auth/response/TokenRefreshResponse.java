package io.github.MichaelAnderson19.TodoAPI.dto.auth.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRefreshResponse {

    private String refreshToken;
    private String jwtToken;

}
