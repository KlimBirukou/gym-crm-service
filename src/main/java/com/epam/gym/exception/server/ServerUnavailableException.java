package com.epam.gym.exception.server;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class ServerUnavailableException extends RuntimeException {

    private final String serverName;
}
