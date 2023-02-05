package io.github.MichaelAnderson19.TodoAPI.dto.auth.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenRequest {

    private String refreshToken;

}
