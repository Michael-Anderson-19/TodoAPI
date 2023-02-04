package io.github.MichaelAnderson19.TodoAPI.configuration.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import org.apache.tomcat.util.http.parser.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try { //can remove this
            final String jwtToken = parseJwtFromRequest(request);
            if (jwtToken != null && jwtUtils.validateToken(jwtToken)) {
                final String userEmail = jwtUtils.extractEmail(jwtToken);
                //if email is set and the securitycontext is empty (no one currently logged in)
                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    //then get the user details from the database
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                    //validate the token against the expiration date and the email inside of the request vs database
                    if (jwtUtils.validateTokenCredentials(jwtToken, userDetails)) {  /////////dis jsut me
                        //create authentication token (authentciation object)
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        //converts the raw java class of the httprequest into an internal spring class (webauthenticationdetailsource)
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        //add the authentication to securitycontext
                        SecurityContextHolder.getContext().setAuthentication(authToken); //this
                    }
                }
            }
        } catch (Exception e) { //can remove this
            System.out.println("CANNOT AUTHENTICATE USER WITH ERROR " + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwtFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
