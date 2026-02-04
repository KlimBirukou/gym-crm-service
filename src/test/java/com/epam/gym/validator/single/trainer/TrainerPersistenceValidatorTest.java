package com.epam.gym.validator.single.trainer;

import com.epam.gym.domain.user.Trainer;
import com.epam.gym.exception.DomainNotFoundException;
import com.epam.gym.mother.TrainerMother;
import com.epam.gym.repository.trainer.ITrainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerPersistenceValidatorTest {

    private final static UUID UID = UUID.randomUUID();
    private final static String FIRSTNAME = "firstname";
    private final static String LASTNAME = "lastname";
    private static final String USERNAME = "username";
    private final static Trainer TRAINER =
        TrainerMother.get(UID, FIRSTNAME, LASTNAME, USERNAME);

    @Mock
    private ITrainerRepository trainerRepository;

    @InjectMocks
    private TrainerPersistenceValidator testObject;

    @Test
    void validate_shouldPass_whenTrainerExist() {
        when(trainerRepository.findByUid(UID))
            .thenReturn(Optional.of(TRAINER));

        assertDoesNotThrow(() -> testObject.validate(UID));
    }

    @Test
    void validate_shouldThrowException_whenTrainerNotExist() {
        when(trainerRepository.findByUid(UID))
            .thenReturn(Optional.empty());

        assertThrows(DomainNotFoundException.class,
            () -> testObject.validate(UID));
    }

    @ParameterizedTest
    @NullSource
    void validate_shouldThrowException_whenDataNull(UUID uid) {
        assertThrows(NullPointerException.class,
            () -> testObject.validate(uid));
    }
}
