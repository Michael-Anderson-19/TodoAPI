package io.github.MichaelAnderson19.TodoAPI.service.security.impl;

import io.github.MichaelAnderson19.TodoAPI.model.security.SecurityUser;
import io.github.MichaelAnderson19.TodoAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository
                .findByEmail(email)
                .map(SecurityUser::new)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                String.format("Error: user with email address %s was not found", email)
                        ));
    }

}
