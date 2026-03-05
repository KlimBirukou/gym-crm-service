package com.epam.gym.repository.mapper;

import com.epam.gym.configuration.ConversionServiceAdapter;
import com.epam.gym.domain.training.Training;
import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.repository.entity.TraineeEntity;
import com.epam.gym.repository.entity.TrainerEntity;
import com.epam.gym.repository.entity.TrainingEntity;
import com.epam.gym.repository.entity.TrainingTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ITrainingEntityToTrainingMapperTest {

    private static final LocalDate TRAINING_DATE = LocalDate.of(2026, 1, 1);
    private static final UUID TRAINING_UID = UUID.randomUUID();
    private static final UUID TRAINEE_UID = UUID.randomUUID();
    private static final UUID TRAINER_UID = UUID.randomUUID();
    private static final UUID TYPE_UID = UUID.randomUUID();
    private static final String TRAINING_NAME = "training_name";
    private static final String TYPE_NAME = "CARDIO";

    @Mock
    private ConversionServiceAdapter conversionServiceAdapter;
    @Mock
    private IDurationMapper durationMapper;

    private ITrainingEntityToTrainingMapper testObject;

    @BeforeEach
    void setUp() {
        testObject = new ITrainingEntityToTrainingMapperImpl(durationMapper, conversionServiceAdapter);
    }

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of(60, Duration.ofMinutes(60)),
            Arguments.of(0, Duration.ZERO),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapEntityToDomain(Integer durationMinutes, Duration expectedDuration) {
        var entity = createEntity(durationMinutes);
        when(durationMapper.toDuration(durationMinutes)).thenReturn(expectedDuration);
        when(conversionServiceAdapter.mapTrainingTypeEntityToTrainingType(entity.getTrainingType()))
            .thenReturn(TrainingType.builder()
                .uid(TYPE_UID)
                .name(TYPE_NAME)
                .build());

        var result = testObject.convert(entity);

        assertNotNull(result);
        assertEquals(TRAINING_UID, result.getUid());
        assertEquals(TRAINEE_UID, result.getTraineeUid());
        assertEquals(TRAINER_UID, result.getTrainerUid());
        assertEquals(TRAINING_NAME, result.getName());
        assertEquals(TRAINING_DATE, result.getDate());
        assertEquals(expectedDuration, result.getDuration());
        assertEquals(TYPE_NAME, result.getTrainingType().getName());
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenArgumentNull(TrainingEntity entity) {
        var result = testObject.convert(entity);

        assertNull(result);
    }

    private static Stream<Arguments> provideInvertConvertData() {
        return Stream.of(
            Arguments.of(Duration.ofMinutes(90), 90),
            Arguments.of(Duration.ZERO, 0),
            Arguments.of(Duration.ofHours(2), 120),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvertConvertData")
    void invertConvert_shouldMapDomainToEntity(Duration duration, Integer expectedMinutes) {
        var trainingType = TrainingType.builder()
            .uid(TYPE_UID)
            .name(TYPE_NAME)
            .build();

        var training = Training.builder()
            .uid(TRAINING_UID)
            .traineeUid(TRAINEE_UID)
            .trainerUid(TRAINER_UID)
            .name(TRAINING_NAME)
            .trainingType(trainingType)
            .date(TRAINING_DATE)
            .duration(duration)
            .build();

        when(durationMapper.toMinutes(duration)).thenReturn(expectedMinutes);

        var result = testObject.invertConvert(training);

        assertNotNull(result);
        assertEquals(TRAINING_UID, result.getUid());
        assertNotNull(result.getTrainee());
        assertEquals(TRAINEE_UID, result.getTrainee().getUid());
        assertNotNull(result.getTrainer());
        assertEquals(TRAINER_UID, result.getTrainer().getUid());
        assertEquals(TRAINING_NAME, result.getName());
        assertEquals(TRAINING_DATE, result.getDate());
        assertEquals(expectedMinutes, result.getDuration());
        assertNotNull(result.getTrainingType());
        assertEquals(TYPE_UID, result.getTrainingType().getUid());
        assertEquals(TYPE_NAME, result.getTrainingType().getName());
    }

    @ParameterizedTest
    @NullSource
    void invertConvert_shouldReturnNull_whenArgumentNull(Training training) {
        var result = testObject.invertConvert(training);

        assertNull(result);
    }

    private static TrainingEntity createEntity(Integer durationMinutes) {
        return TrainingEntity.builder()
            .uid(TRAINING_UID)
            .trainee(TraineeEntity.builder()
                .uid(TRAINEE_UID)
                .build())
            .trainer(TrainerEntity.builder()
                .uid(TRAINER_UID)
                .build())
            .name(TRAINING_NAME)
            .trainingType(TrainingTypeEntity.builder()
                .uid(TYPE_UID)
                .name(TYPE_NAME)
                .build())
            .date(TRAINING_DATE)
            .duration(durationMinutes)
            .build();
    }
}
