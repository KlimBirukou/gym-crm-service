package com.epam.gym.repository.domain.assignment;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.entity.TraineeEntity;
import com.epam.gym.repository.entity.TraineeTrainerEntity;
import com.epam.gym.repository.entity.TrainerEntity;
import com.epam.gym.repository.jpa.assignment.IAssignmentEntityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class JpaAssignmentRepositoryTest {

    private static final String TRAINEE_USERNAME = "trainee_username";
    private static final String TRAINER_USERNAME = "trainer_username";
    private static final Trainee TRAINEE = Trainee.builder()
        .username(TRAINEE_USERNAME)
        .build();
    private static final Trainer TRAINER = Trainer.builder()
        .username(TRAINER_USERNAME)
        .build();
    private static final TraineeEntity TRAINEE_ENTITY = mock(TraineeEntity.class);
    private static final TraineeEntity TRAINEE_ENTITY_2 = new TraineeEntity();
    private static final TrainerEntity TRAINER_ENTITY = new TrainerEntity();
    private static final TrainerEntity TRAINER_ENTITY_2 = new TrainerEntity();

    @Mock
    private IAssignmentEntityRepository repository;
    @Mock
    private ConversionService conversionService;

    @Captor
    private ArgumentCaptor<TraineeTrainerEntity> trainerEntityArgumentCaptor;

    @InjectMocks
    private JpaAssignmentRepository testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(repository, conversionService);
    }

    @Test
    void assign_shouldSaveEntity_whenAlways() {
        doReturn(TRAINEE_ENTITY).when(conversionService).convert(TRAINEE, TraineeEntity.class);
        doReturn(TRAINER_ENTITY).when(conversionService).convert(TRAINER, TrainerEntity.class);

        testObject.assign(TRAINEE, TRAINER);

        verify(repository).save(trainerEntityArgumentCaptor.capture());
        var capturedEntity = trainerEntityArgumentCaptor.getValue();

        assertEquals(TRAINEE_ENTITY, capturedEntity.getTrainee());
        assertEquals(TRAINER_ENTITY, capturedEntity.getTrainer());
    }

    private static Stream<Arguments> provideNullsForAssign() {
        return Stream.of(
            Arguments.of(null, TRAINER),
            Arguments.of(TRAINEE, null),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullsForAssign")
    void assign_shouldThrowException_whenArgumentsNull(Trainee trainee, Trainer trainer) {
        assertThrows(NullPointerException.class, () -> testObject.assign(trainee, trainer));
    }

    @Test
    void checkAssign_shouldReturnTrue_whenExists() {
        doReturn(true).when(repository)
            .existsByTraineeUserUsernameAndTrainerUserUsername(TRAINEE_USERNAME, TRAINER_USERNAME);

        var result = testObject.checkAssign(TRAINEE_USERNAME, TRAINER_USERNAME);

        assertTrue(result);
    }

    private static Stream<Arguments> provideNullsForCheckAssign() {
        return Stream.of(
            Arguments.of(null, TRAINER_USERNAME),
            Arguments.of(TRAINEE_USERNAME, null),
            Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullsForCheckAssign")
    void checkAssign_shouldThrowException_whenArgumentsNull(String traineeUsername, String trainerUsername) {
        assertThrows(NullPointerException.class, () -> testObject.checkAssign(traineeUsername, trainerUsername));
    }

    @Test
    void getTrainees_shouldReturnEmptyList() {
        doReturn(List.of()).when(repository).getTrainees(TRAINER_USERNAME, true, true);

        var result = testObject.getTrainees(TRAINER_USERNAME, true, true);

        assertTrue(result.isEmpty());
        verify(repository).getTrainees(TRAINER_USERNAME, true, true);
        verifyNoInteractions(conversionService);
    }

    private static Stream<Arguments> provideTraineesData() {
        return Stream.of(
            Arguments.of(List.of(TRAINEE_ENTITY)),
            Arguments.of(List.of(TRAINEE_ENTITY, TRAINEE_ENTITY_2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTraineesData")
    void getTrainees_shouldReturnConvertedList_whenRepositoryReturnsData(List<TraineeEntity> entities) {
        doReturn(entities).when(repository).getTrainees(eq(TRAINER_USERNAME), anyBoolean(), anyBoolean());
        entities.forEach(entity ->
            doReturn(new Trainee()).when(conversionService).convert(entity, Trainee.class)
        );

        var result = testObject.getTrainees(TRAINER_USERNAME, true, true);

        assertEquals(entities.size(), result.size());
        verify(repository).getTrainees(TRAINER_USERNAME, true, true);
        verify(conversionService, times(entities.size())).convert(any(TraineeEntity.class), eq(Trainee.class));
    }

    private static Stream<Arguments> provideNullsForGetTrainees() {
        return Stream.of(
            Arguments.of(null, true, true),
            Arguments.of(TRAINER_USERNAME, null, true),
            Arguments.of(TRAINER_USERNAME, true, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullsForGetTrainees")
    void getTrainees_shouldThrowException_whenArgumentsNull(String trainerUsername, Boolean assigned, Boolean active) {
        assertThrows(NullPointerException.class, () -> testObject.getTrainees(trainerUsername, assigned, active));
    }

    @Test
    void getTrainers_shouldReturnEmptyList() {
        doReturn(List.of()).when(repository).getTrainers(TRAINEE_USERNAME, true, true);

        var result = testObject.getTrainers(TRAINEE_USERNAME, true, true);

        assertTrue(result.isEmpty());
        verify(repository).getTrainers(TRAINEE_USERNAME, true, true);
        verifyNoInteractions(conversionService);
    }

    private static Stream<Arguments> provideTrainersData() {
        return Stream.of(
            Arguments.of(List.of(TRAINER_ENTITY)),
            Arguments.of(List.of(TRAINER_ENTITY, TRAINER_ENTITY_2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTrainersData")
    void getTrainers_shouldReturnConvertedList_whenRepositoryReturnsData(List<TrainerEntity> entities) {
        doReturn(entities).when(repository).getTrainers(eq(TRAINEE_USERNAME), anyBoolean(), anyBoolean());
        entities.forEach(entity ->
            doReturn(new Trainer()).when(conversionService).convert(entity, Trainer.class)
        );

        var result = testObject.getTrainers(TRAINEE_USERNAME, true, true);

        assertEquals(entities.size(), result.size());
        verify(repository).getTrainers(TRAINEE_USERNAME, true, true);
        verify(conversionService, times(entities.size())).convert(any(TrainerEntity.class), eq(Trainer.class));
    }

    private static Stream<Arguments> provideNullsForGetTrainers() {
        return Stream.of(
            Arguments.of(null, true, true),
            Arguments.of(TRAINEE_USERNAME, null, true),
            Arguments.of(TRAINEE_USERNAME, true, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullsForGetTrainers")
    void getTrainers_shouldThrowException_whenArgumentsNull(String traineeUsername, Boolean assigned, Boolean active) {
        assertThrows(NullPointerException.class, () -> testObject.getTrainers(traineeUsername, assigned, active));
    }
}
