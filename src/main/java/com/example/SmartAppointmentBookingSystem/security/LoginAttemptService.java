package com.example.SmartAppointmentBookingSystem.security;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);

    private final ConcurrentHashMap<String, LoginAttempt> attempts = new ConcurrentHashMap<>();

    public void loginFailed(String username) {
        LoginAttempt attempt = attempts.getOrDefault(username, new LoginAttempt());
        attempt.failedAttempts++;
        attempt.lastFailedAt = LocalDateTime.now();
        if (attempt.failedAttempts >= MAX_ATTEMPTS) {
            attempt.locked = true;
        }
        attempts.put(username, attempt);
    }

    public void loginSucceeded(String username) {
        attempts.remove(username); // reset
    }

    public boolean isBlocked(String username) {
        LoginAttempt attempt = attempts.get(username);
        if (attempt == null) return false;

        if (attempt.locked && attempt.lastFailedAt.plus(LOCK_DURATION).isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
    }

    private static class LoginAttempt {
        int failedAttempts = 0;
        LocalDateTime lastFailedAt;
        boolean locked = false;
    }
}
