package com.epam.gym.crm.exception.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountTemporarilyBlockedException extends RuntimeException {

    private final long minutesRemaining;
}
