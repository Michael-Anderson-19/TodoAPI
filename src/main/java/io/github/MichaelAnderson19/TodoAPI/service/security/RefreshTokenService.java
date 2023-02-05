package io.github.MichaelAnderson19.TodoAPI.service.security;

import io.github.MichaelAnderson19.TodoAPI.model.security.RefreshToken;

public interface RefreshTokenService {

    RefreshToken findByToken(String token);


    String createRefreshToken(String userEmail);

    RefreshToken verifyRefreshTokenExpirationDate(RefreshToken token);

    void deleteToken(String userEmail);

    void deleteIfExists(String email);
}
