package com.epam.gym.security;

import lombok.NonNull;

public interface IJwtService {

    String generateToken(@NonNull String username);

    String extractUsername(@NonNull String token);

    boolean isTokenValid(@NonNull String token);
}
