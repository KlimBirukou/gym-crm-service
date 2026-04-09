package com.epam.gym.repository.mapper;

import com.epam.gym.domain.auth.LoginAttempt;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ILoginAttemptEntityToLoginAttemptMapperTest {

    private static final UUID USER_UID = UUID.randomUUID();
    private static final LocalDateTime LAST_FAILED_AT = LocalDateTime.of(2026, 3, 1, 12, 0);

    private final ILoginAttemptEntityToLoginAttemptMapper testObject =
        Mappers.getMapper(ILoginAttemptEntityToLoginAttemptMapper.class);

    static Stream<Arguments> provideEntityToDomainData() {
        return Stream.of(
            Arguments.of(1, LAST_FAILED_AT),
            Arguments.of(3, LAST_FAILED_AT),
            Arguments.of(0, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideEntityToDomainData")
    void convert_shouldMapEntityToDomain(int failedAttempts, LocalDateTime lastFailedAt) {
        var entity = LoginAttemptEntity.builder()
            .userUid(USER_UID)
            .failedAttempts(failedAttempts)
            .lastFailedAt(lastFailedAt)
            .build();

        var result = testObject.convert(entity);

        assertNotNull(result);
        assertEquals(USER_UID, result.getUserUid());
        assertEquals(failedAttempts, result.getFailedAttempts());
        assertEquals(lastFailedAt, result.getLastFailedAt());
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenEntityArgumentNull(LoginAttemptEntity entity) {
        var result = testObject.convert(entity);

        assertNull(result);
    }

    static Stream<Arguments> provideDomainToEntityData() {
        return Stream.of(
            Arguments.of(1, LAST_FAILED_AT),
            Arguments.of(3, LAST_FAILED_AT),
            Arguments.of(0, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDomainToEntityData")
    void convert_shouldMapDomainToEntity(int failedAttempts, LocalDateTime lastFailedAt) {
        var domain = LoginAttempt.builder()
            .userUid(USER_UID)
            .failedAttempts(failedAttempts)
            .lastFailedAt(lastFailedAt)
            .build();

        var result = testObject.convert(domain);

        assertNotNull(result);
        assertEquals(USER_UID, result.getUserUid());
        assertEquals(failedAttempts, result.getFailedAttempts());
        assertEquals(lastFailedAt, result.getLastFailedAt());
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenDomainArgumentNull(LoginAttempt domain) {
        var result = testObject.convert(domain);

        assertNull(result);
    }
}
