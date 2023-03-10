package io.github.MichaelAnderson19.TodoAPI.service.security.impl;

import io.github.MichaelAnderson19.TodoAPI.configuration.security.JwtUtils;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.LoginRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.request.RefreshTokenRequest;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.response.JwtResponse;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.response.TokenRefreshResponse;
import io.github.MichaelAnderson19.TodoAPI.dto.response.UserDto;
import io.github.MichaelAnderson19.TodoAPI.model.User;
import io.github.MichaelAnderson19.TodoAPI.model.security.RefreshToken;
import io.github.MichaelAnderson19.TodoAPI.model.security.SecurityUser;
import io.github.MichaelAnderson19.TodoAPI.service.UserService;
import io.github.MichaelAnderson19.TodoAPI.service.impl.UserServiceImpl;
import io.github.MichaelAnderson19.TodoAPI.service.security.AuthService;
import io.github.MichaelAnderson19.TodoAPI.service.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Override
    public JwtResponse login(LoginRequestDto loginRequest) {

        Authentication authentication = authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

        setSecurityContextHolder(authentication);

        SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateJwt(userDetails);
        refreshTokenService.deleteIfExists(userDetails.getUsername());
        String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
        return JwtResponse
                .builder()
                .userEmail(userDetails.getUsername())
                .jwtToken(jwtToken)
                .refreshToken(refreshToken)
                .role(userDetails.getRoleString())
                .build();
    }

    public TokenRefreshResponse refreshJwtToken(RefreshTokenRequest refreshTokenRequest) {

        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken());
        refreshTokenService.verifyRefreshTokenExpirationDate(refreshToken);

        User user = refreshToken.getUser();
        String jwtToken = jwtUtils.generateTokenFromEmail(user.getEmail());
        return TokenRefreshResponse.builder()
                .jwtToken(jwtToken)
                .refreshToken(refreshToken.getToken()).build();
    }

    private Authentication authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        return authentication;
    }

    public void logoutUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!userEmail.equals("anonymousUser"))
            refreshTokenService.deleteIfExists(userEmail);
    }

    private void setSecurityContextHolder(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public UserDto getCurrentUser() {
        return userService.getUserDto(getCurrentUserEmail());
    }

}
