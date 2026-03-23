package com.epam.gym.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LoginAttempt {

    private UUID userUid;
    private int failedAttempts;
    private LocalDateTime lastFailedAt;

    public boolean isBlocked(int maxAttempts, @NonNull Duration blockDuration) {
        return Optional.ofNullable(lastFailedAt)
            .filter(last -> failedAttempts >= maxAttempts)
            .map(last -> last.plus(blockDuration).isAfter(LocalDateTime.now()))
            .orElse(false);
    }

    public boolean isExpired(int maxAttempts, @NonNull Duration blockDuration) {
        return Optional.ofNullable(lastFailedAt)
            .filter(last -> failedAttempts >= maxAttempts)
            .map(last -> !last.plus(blockDuration).isAfter(LocalDateTime.now()))
            .orElse(false);
    }

    public long minutesUntilUnblock(@NonNull Duration blockDuration) {
        return Optional.ofNullable(lastFailedAt)
            .map(last -> last.plus(blockDuration))
            .map(unblockTime -> ChronoUnit.MINUTES.between(LocalDateTime.now(), unblockTime) + 1)
            .filter(minutes -> minutes > 0)
            .orElse(0L);
    }

    public void recordFailure() {
        this.failedAttempts++;
        this.lastFailedAt = LocalDateTime.now();
    }

    public void reset() {
        this.failedAttempts = 0;
        this.lastFailedAt = null;
    }
}
