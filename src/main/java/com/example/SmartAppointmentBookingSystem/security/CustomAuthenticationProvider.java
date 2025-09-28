package com.example.SmartAppointmentBookingSystem.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        if (loginAttemptService.isBlocked(username)) {
            throw new LockedException("Too many failed login attempts. Try again later.");
        }

        UserDetails user = userDetailsService.loadUserByUsername(username);
        String rawPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            loginAttemptService.loginFailed(username);
            throw new BadCredentialsException("Invalid credentials");
        }

        loginAttemptService.loginSucceeded(username);

        return new UsernamePasswordAuthenticationToken(user, rawPassword, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
