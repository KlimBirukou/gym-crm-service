package com.epam.gym.service.auth.jwt;

import lombok.NonNull;

public interface IJwtService {

    String generateToken(@NonNull String username);

    String extractUsername(@NonNull String token);

    boolean isTokenValid(@NonNull String token);
}
