package com.epam.gym.validator.single.training;

import com.epam.gym.domain.training.Training;
import com.epam.gym.v1.repository.training.ITrainingRepository;
import com.epam.gym.mother.TrainingMother;
import com.epam.gym.mother.dto.training.CreateTrainingDtoMother;
import com.epam.gym.v1.service.training.dto.CreateTrainingDto;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAvailabilityOnDateValidatorTest {

    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    private static final String NAME = "name";
    private static final UUID TARGET_TRAINEE_UID = UUID.randomUUID();
    private static final UUID TARGET_TRAINER_UID = UUID.randomUUID();
    private static final UUID OTHER_TRAINEE_UID = UUID.randomUUID();
    private static final UUID OTHER_TRAINER_UID = UUID.randomUUID();
    private static final CreateTrainingDto CREATE_TRAINING_DTO =
        CreateTrainingDtoMother.get(TARGET_TRAINEE_UID, TARGET_TRAINER_UID, NAME, DATE);
    private static final Training TRAINING_WITH_TARGET_TRAINEE = TrainingMother
        .getWithTraineeUid(TARGET_TRAINEE_UID);
    private static final Training TRAINING_WITH_TARGET_TRAINER = TrainingMother
        .getWithTrainerUid(TARGET_TRAINER_UID);
    private static final Training TRAINING_WITH_OTHER_TRAINEE = TrainingMother
        .getWithTraineeUid(OTHER_TRAINEE_UID);
    private static final Training TRAINING_WITH_OTHER_TRAINER = TrainingMother
        .getWithTrainerUid(OTHER_TRAINER_UID);

    @Mock
    private ITrainingRepository trainingRepository;

    @InjectMocks
    private UserAvailabilityOnDateValidator testObject;

    private static Stream<List<Training>> provideAgreementTestData() {
        return Stream.of(
            List.of(),
            List.of(TRAINING_WITH_OTHER_TRAINEE),
            List.of(TRAINING_WITH_OTHER_TRAINER),
            List.of(TRAINING_WITH_OTHER_TRAINEE, TRAINING_WITH_OTHER_TRAINER)
        );
    }

    @ParameterizedTest
    @MethodSource("provideAgreementTestData")
    void validate_shouldPass_whenNoConflicts(List<Training> list) {
        when(trainingRepository.findByDate(DATE))
            .thenReturn(list);

        assertDoesNotThrow(() -> testObject.validate(CREATE_TRAINING_DTO));
    }

    private static Stream<List<Training>> provideConflictTestData() {
        return Stream.of(
            List.of(TRAINING_WITH_TARGET_TRAINEE),
            List.of(TRAINING_WITH_TARGET_TRAINER),
            List.of(TRAINING_WITH_TARGET_TRAINEE, TRAINING_WITH_TARGET_TRAINER)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConflictTestData")
    void validate_shouldThrowException_whenHasConflicts(List<Training> list) {
        when(trainingRepository.findByDate(DATE))
            .thenReturn(list);

        assertThrows(EntityBusyOnDateException.class,
            () -> testObject.validate(CREATE_TRAINING_DTO));
    }

    @ParameterizedTest
    @NullSource
    void validate_shouldThrowException_whenDataNull(CreateTrainingDto dto) {
        assertThrows(NullPointerException.class,
            () -> testObject.validate(dto));
    }
}
