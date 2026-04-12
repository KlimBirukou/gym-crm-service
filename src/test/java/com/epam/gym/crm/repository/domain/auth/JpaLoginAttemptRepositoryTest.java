package com.epam.gym.crm.repository.domain.auth;

import com.epam.gym.crm.domain.auth.LoginAttempt;
import com.epam.gym.repository.jpa.auth.ILoginAttemptsEntityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class JpaLoginAttemptRepositoryTest {

    private static final UUID USER_UID = UUID.randomUUID();
    private static final LoginAttempt LOGIN_ATTEMPT = LoginAttempt.builder()
        .userUid(USER_UID)
        .failedAttempts(1)
        .lastFailedAt(LocalDateTime.of(2026, 3, 1, 12, 0))
        .build();
    private static final LoginAttemptEntity LOGIN_ATTEMPT_ENTITY = LoginAttemptEntity.builder()
        .userUid(USER_UID)
        .failedAttempts(1)
        .lastFailedAt(LocalDateTime.of(2026, 3, 1, 12, 0))
        .build();

    @Mock
    private ILoginAttemptsEntityRepository repository;
    @Mock
    private ConversionService conversionService;

    @Captor
    private ArgumentCaptor<LoginAttemptEntity> loginAttemptEntityCaptor;

    @InjectMocks
    private JpaLoginAttemptRepository testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(repository, conversionService);
    }

    @Test
    void findByUserUid_shouldReturnLoginAttempt_whenEntityExists() {
        doReturn(Optional.of(LOGIN_ATTEMPT_ENTITY)).when(repository).findById(USER_UID);
        doReturn(LOGIN_ATTEMPT).when(conversionService).convert(LOGIN_ATTEMPT_ENTITY, LoginAttempt.class);

        var result = testObject.findByUserUid(USER_UID);

        assertTrue(result.isPresent());
        assertSame(LOGIN_ATTEMPT, result.get());
    }

    @Test
    void findByUserUid_shouldReturnEmpty_whenEntityNotExists() {
        doReturn(Optional.empty()).when(repository).findById(USER_UID);

        var result = testObject.findByUserUid(USER_UID);

        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    void findByUserUid_shouldThrowException_whenArgumentNull(UUID userUid) {
        assertThrows(NullPointerException.class, () -> testObject.findByUserUid(userUid));
    }

    @Test
    void save_shouldConvertAndSaveEntity() {
        doReturn(LOGIN_ATTEMPT_ENTITY).when(conversionService).convert(LOGIN_ATTEMPT, LoginAttemptEntity.class);

        testObject.save(LOGIN_ATTEMPT);

        verify(repository).save(loginAttemptEntityCaptor.capture());
        var capturedEntity = loginAttemptEntityCaptor.getValue();

        assertEquals(LOGIN_ATTEMPT_ENTITY, capturedEntity);
    }

    @ParameterizedTest
    @NullSource
    void save_shouldThrowException_whenArgumentNull(LoginAttempt loginAttempt) {
        assertThrows(NullPointerException.class, () -> testObject.save(loginAttempt));
    }

    @Test
    void deleteByUserUid_shouldDeleteEntity() {
        doNothing().when(repository).deleteById(USER_UID);

        testObject.deleteByUserUid(USER_UID);

        verify(repository).deleteById(USER_UID);
    }

    @ParameterizedTest
    @NullSource
    void deleteByUserUid_shouldThrowException_whenArgumentNull(UUID userUid) {
        assertThrows(NullPointerException.class, () -> testObject.deleteByUserUid(userUid));
    }
}
