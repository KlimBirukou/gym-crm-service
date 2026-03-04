package com.epam.gym.facade.training;

import com.epam.gym.controller.rest.training.dto.request.CreateTrainingRequest;
import com.epam.gym.controller.rest.training.dto.request.GetTraineeTrainingsRequest;
import com.epam.gym.controller.rest.training.dto.request.GetTrainerTrainingRequest;
import com.epam.gym.controller.rest.training.dto.response.TrainingTypeResponse;
import com.epam.gym.domain.training.Training;
import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainer.ITrainerService;
import com.epam.gym.service.training.ITrainingService;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import com.epam.gym.service.training.dto.TraineeTrainingsDto;
import com.epam.gym.service.training.dto.TrainerTrainingsDto;
import com.epam.gym.service.type.ITrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TrainingFacadeTest {

    private static final UUID TRAINEE_UID = UUID.randomUUID();
    private static final UUID TRAINER_UID = UUID.randomUUID();
    private static final UUID TRAINING_TYPE_UID = UUID.randomUUID();
    private static final String TRAINEE_USERNAME = "trainee_username";
    private static final String TRAINER_USERNAME = "trainer_username";
    private static final String TRAINING_NAME = "training_name";
    private static final String TRAINING_TYPE_NAME = "training_type_name";
    private static final LocalDate DATE = LocalDate.of(2026, 3, 1);
    private static final LocalDate FROM_DATE = LocalDate.of(2026, 2, 1);
    private static final LocalDate TO_DATE = LocalDate.of(2026, 3, 31);
    private static final int DURATION_MINUTES = 60;

    @Mock
    private ITraineeService traineeService;
    @Mock
    private ITrainerService trainerService;
    @Mock
    private ITrainingService trainingService;
    @Mock
    private ITrainingTypeService trainingTypeService;
    @Mock
    private ConversionService conversionService;

    private TrainingFacade testObject;

    @BeforeEach
    void setUp() {
        testObject = new TrainingFacade(
            traineeService,
            trainerService,
            trainingService,
            trainingTypeService,
            conversionService
        );
    }

    @Test
    void create_shouldCallTrainingService_whenAlways() {
        var request = getCreateTrainingRequest();
        var createTrainingDto = getCreateTrainingDto();
        var training = getTraining();
        doReturn(createTrainingDto).when(conversionService).convert(request, CreateTrainingDto.class);
        doReturn(training).when(trainingService).create(createTrainingDto);

        testObject.create(request);

        verify(trainingService).create(createTrainingDto);

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void create_shouldThrowException_whenRequestNull(CreateTrainingRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.create(request));

        assertNoUnexpectedInteractions();
    }


    @Test
    void getTraineeTrainings_shouldReturnMappedList_whenTrainingsExist() {
        var request = getTraineeTrainingsRequest();
        var dto = getTraineeTrainingsDto();
        var training = getTraining();
        var trainer = getTrainer();
        doReturn(dto).when(conversionService).convert(request, TraineeTrainingsDto.class);
        doReturn(List.of(training)).when(trainingService).getTraineeTrainings(dto);
        doReturn(List.of(trainer)).when(trainerService).getByUids(List.of(TRAINER_UID));

        var result = testObject.getTraineeTrainings(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TRAINING_NAME, result.getFirst().name());
        assertEquals(DATE, result.getFirst().date());
        assertEquals(TRAINING_TYPE_NAME, result.getFirst().trainingTypeName());
        assertEquals(DURATION_MINUTES, result.getFirst().duration());
        assertEquals(TRAINER_USERNAME, result.getFirst().trainerUsername());

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void getTraineeTrainings_shouldThrowException_whenRequestNull(GetTraineeTrainingsRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.getTraineeTrainings(request));

        assertNoUnexpectedInteractions();
    }


    @Test
    void getTrainerTrainings_shouldReturnMappedList_whenTrainingsExist() {
        var request = getTrainerTrainingsRequest();
        var dto = getTrainerTrainingsDto();
        var training = getTraining();
        var trainee = getTrainee();
        doReturn(dto).when(conversionService).convert(request, TrainerTrainingsDto.class);
        doReturn(List.of(training)).when(trainingService).getTrainerTrainings(dto);
        doReturn(List.of(trainee)).when(traineeService).getByUids(List.of(TRAINEE_UID));

        var result = testObject.getTrainerTrainings(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TRAINING_NAME, result.getFirst().name());
        assertEquals(DATE, result.getFirst().date());
        assertEquals(TRAINING_TYPE_NAME, result.getFirst().trainingTypeName());
        assertEquals(DURATION_MINUTES, result.getFirst().duration());
        assertEquals(TRAINEE_USERNAME, result.getFirst().traineeUsername());

        assertNoUnexpectedInteractions();
    }

    @ParameterizedTest
    @NullSource
    void getTrainerTrainings_shouldThrowException_whenRequestNull(GetTrainerTrainingRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.getTrainerTrainings(request));

        assertNoUnexpectedInteractions();
    }


    @Test
    void getTrainingsTypes_shouldReturnMappedList_whenTypesExist() {
        var trainingType = getTrainingType();
        var trainingTypeResponse = getTrainingTypeResponse();
        doReturn(List.of(trainingType)).when(trainingTypeService).getAll();
        doReturn(trainingTypeResponse).when(conversionService).convert(trainingType, TrainingTypeResponse.class);

        var result = testObject.getTrainingsTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TRAINING_TYPE_UID, result.getFirst().uid());
        assertEquals(TRAINING_TYPE_NAME, result.getFirst().name());

        assertNoUnexpectedInteractions();
    }

    private static CreateTrainingRequest getCreateTrainingRequest() {
        return CreateTrainingRequest.builder()
            .traineeUsername(TRAINEE_USERNAME)
            .trainerUsername(TRAINER_USERNAME)
            .name(TRAINING_NAME)
            .date(DATE)
            .durationInMinutes(DURATION_MINUTES)
            .build();
    }

    private static CreateTrainingDto getCreateTrainingDto() {
        return CreateTrainingDto.builder()
            .traineeUsername(TRAINEE_USERNAME)
            .trainerUsername(TRAINER_USERNAME)
            .name(TRAINING_NAME)
            .date(DATE)
            .durationInMinutes(DURATION_MINUTES)
            .build();
    }

    private static GetTraineeTrainingsRequest getTraineeTrainingsRequest() {
        return GetTraineeTrainingsRequest.builder()
            .username(TRAINEE_USERNAME)
            .from(FROM_DATE)
            .to(TO_DATE)
            .trainerUsername(TRAINER_USERNAME)
            .trainingTypeName(TRAINING_TYPE_NAME)
            .build();
    }

    private static TraineeTrainingsDto getTraineeTrainingsDto() {
        return TraineeTrainingsDto.builder()
            .username(TRAINEE_USERNAME)
            .from(FROM_DATE)
            .to(TO_DATE)
            .trainerUsername(TRAINER_USERNAME)
            .trainingTypeName(TRAINING_TYPE_NAME)
            .build();
    }

    private static GetTrainerTrainingRequest getTrainerTrainingsRequest() {
        return GetTrainerTrainingRequest.builder()
            .username(TRAINER_USERNAME)
            .from(FROM_DATE)
            .to(TO_DATE)
            .traineeUsername(TRAINEE_USERNAME)
            .build();
    }

    private static TrainerTrainingsDto getTrainerTrainingsDto() {
        return TrainerTrainingsDto.builder()
            .username(TRAINER_USERNAME)
            .from(FROM_DATE)
            .to(TO_DATE)
            .traineeUsername(TRAINEE_USERNAME)
            .build();
    }

    private static Training getTraining() {
        return Training.builder()
            .uid(UUID.randomUUID())
            .traineeUid(TRAINEE_UID)
            .trainerUid(TRAINER_UID)
            .name(TRAINING_NAME)
            .trainingType(getTrainingType())
            .date(DATE)
            .duration(Duration.ofMinutes(DURATION_MINUTES))
            .build();
    }

    private static TrainingType getTrainingType() {
        return TrainingType.builder()
            .uid(TRAINING_TYPE_UID)
            .name(TRAINING_TYPE_NAME)
            .build();
    }

    private static TrainingTypeResponse getTrainingTypeResponse() {
        return TrainingTypeResponse.builder()
            .uid(TRAINING_TYPE_UID)
            .name(TRAINING_TYPE_NAME)
            .build();
    }

    private static Trainee getTrainee() {
        return Trainee.builder()
            .uid(TRAINEE_UID)
            .username(TRAINEE_USERNAME)
            .build();
    }

    private static Trainer getTrainer() {
        return Trainer.builder()
            .uid(TRAINER_UID)
            .username(TRAINER_USERNAME)
            .build();
    }

    private void assertNoUnexpectedInteractions() {
        verifyNoMoreInteractions(
            traineeService,
            trainerService,
            trainingService,
            trainingTypeService,
            conversionService
        );
    }
}
