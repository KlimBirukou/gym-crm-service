package com.epam.gym.exception.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountTemporarilyBlockedException extends RuntimeException {

    private final long minutesRemaining;
}
