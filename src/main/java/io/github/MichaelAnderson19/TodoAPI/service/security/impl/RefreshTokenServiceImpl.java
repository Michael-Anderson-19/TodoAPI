package io.github.MichaelAnderson19.TodoAPI.service.security.impl;

import io.github.MichaelAnderson19.TodoAPI.exception.InvalidTokenException;
import io.github.MichaelAnderson19.TodoAPI.model.User;
import io.github.MichaelAnderson19.TodoAPI.model.security.RefreshToken;
import io.github.MichaelAnderson19.TodoAPI.repository.RefreshTokenRepository;
import io.github.MichaelAnderson19.TodoAPI.service.impl.UserServiceImpl;
import io.github.MichaelAnderson19.TodoAPI.service.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${app.refreshExpirationSec}") //@Value("${app.jwtSecret}")
    private Long refreshTokenExpirationSeconds;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserServiceImpl userService;

    @Override
    public RefreshToken findByToken(String token) {
        //could get user and return this if needed?
        return refreshTokenRepository.findByToken(token).orElseThrow(() -> new InvalidTokenException("Refresh token bot found"));
    }

    @Override
    public String createRefreshToken(String userEmail) {
        //public RefreshToken createRefreshToken(String userEmail) {

        User user = userService.getUser(userEmail);
        RefreshToken refreshToken = refreshTokenRepository.save(RefreshToken.builder()
                .user(user)
                .expiryDate(Instant.now().plusSeconds(refreshTokenExpirationSeconds))
                .token(UUID.randomUUID().toString())
                .build());
        return refreshToken.getToken();
    }

    @Override
    public RefreshToken verifyRefreshTokenExpirationDate(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new InvalidTokenException("Refresh token has expired");
        }
        return token; //return true?;
    }

    @Override
    public void deleteToken(String email) {
        refreshTokenRepository.deleteByUserEmail(email);
    }

    //@Scope(proxyMode = ScopedProxyMode.INTERFACES) to the DAO
//    @Transactional
    public void deleteIfExists(String email) {
        refreshTokenRepository.findByUserEmail(email).ifPresent(refreshTokenRepository::delete);
    }

}
