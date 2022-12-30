package io.github.MichaelAnderson19.TodoAPI.service.security;

import io.github.MichaelAnderson19.TodoAPI.configuration.security.JwtUtils;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.LoginRequestDto;
import io.github.MichaelAnderson19.TodoAPI.dto.auth.LoginResponseDto;
import io.github.MichaelAnderson19.TodoAPI.model.security.SecurityUser;
import io.github.MichaelAnderson19.TodoAPI.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public LoginResponseDto login(LoginRequestDto loginRequest) {

            Authentication authentication = authenticateUser(loginRequest.getUserEmail(), loginRequest.getUserPassword());

            setSecurityContextHolder(authentication);

            SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
            String jwtToken = jwtUtils.generateJwt(userDetails);
            return LoginResponseDto
                    .builder()
                    .userEmail(userDetails.getUsername())
                    .jwtToken(jwtToken)
                    .build();
    }

    private Authentication authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        return authentication;
    }

    private void setSecurityContextHolder(Authentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
