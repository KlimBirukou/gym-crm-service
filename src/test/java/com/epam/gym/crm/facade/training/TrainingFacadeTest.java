package com.epam.gym.crm.facade.training;

import com.epam.gym.crm.sender.EventType;
import com.epam.gym.crm.sender.ITrainerWorkloadUpdateEventSender;
import com.epam.gym.crm.controller.rest.training.dto.request.CreateTrainingRequest;
import com.epam.gym.crm.controller.rest.training.dto.request.GetTraineeTrainingsRequest;
import com.epam.gym.crm.controller.rest.training.dto.request.GetTrainerTrainingRequest;
import com.epam.gym.crm.controller.rest.training.dto.response.TrainingTypeResponse;
import com.epam.gym.crm.domain.training.Training;
import com.epam.gym.crm.domain.training.TrainingType;
import com.epam.gym.crm.domain.user.Trainee;
import com.epam.gym.crm.domain.user.Trainer;
import com.epam.gym.crm.service.trainee.ITraineeService;
import com.epam.gym.crm.service.trainer.ITrainerService;
import com.epam.gym.crm.service.training.ITrainingService;
import com.epam.gym.crm.service.training.dto.CreateTrainingDto;
import com.epam.gym.crm.service.training.dto.TraineeTrainingsDto;
import com.epam.gym.crm.service.training.dto.TrainerTrainingsDto;
import com.epam.gym.crm.service.type.ITrainingTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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
    private ITrainerWorkloadUpdateEventSender trainerWorkloadService;
    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private TrainingFacade testObject;

    @Test
    void
    create_shouldConvertAndCreateTrainingAndNotify_whenRequestIsValid() {
        var request = buildCreateTrainingRequest();
        var createTrainingDto = buildCreateTrainingDto();
        var training = buildTraining();
        doReturn(createTrainingDto).when(conversionService).convert(request, CreateTrainingDto.class);
        doReturn(training).when(trainingService).create(createTrainingDto);
        doNothing().when(trainerWorkloadService).notify(training, createTrainingDto.trainerUsername(), EventType.ADD);

        testObject.create(request);

        verify(conversionService).convert(request, CreateTrainingDto.class);
        verify(trainingService).create(createTrainingDto);
        verify(trainerWorkloadService).notify(training, createTrainingDto.trainerUsername(), EventType.ADD);
    }

    @ParameterizedTest
    @NullSource
    void create_shouldThrowException_whenRequestNull(CreateTrainingRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.create(request));
    }

    @Test
    void getTraineeTrainings_shouldReturnMappedList_whenTrainingsExist() {
        var request = buildTraineeTrainingsRequest();
        var dto = buildTraineeTrainingsDto();
        var training = buildTraining();
        var trainer = buildTrainer();
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
    }

    @ParameterizedTest
    @NullSource
    void getTraineeTrainings_shouldThrowException_whenRequestNull(GetTraineeTrainingsRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.getTraineeTrainings(request));
    }

    @Test
    void getTrainerTrainings_shouldReturnMappedList_whenTrainingsExist() {
        var request = buildTrainerTrainingsRequest();
        var dto = buildTrainerTrainingsDto();
        var training = buildTraining();
        var trainee = buildTrainee();
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
    }

    @ParameterizedTest
    @NullSource
    void getTrainerTrainings_shouldThrowException_whenRequestNull(GetTrainerTrainingRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.getTrainerTrainings(request));
    }

    @Test
    void getTrainingsTypes_shouldReturnMappedList_whenTypesExist() {
        var trainingType = buildTrainingType();
        var trainingTypeResponse = buildTrainingTypeResponse();
        doReturn(List.of(trainingType)).when(trainingTypeService).getAll();
        doReturn(trainingTypeResponse).when(conversionService).convert(trainingType, TrainingTypeResponse.class);

        var result = testObject.getTrainingsTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(TRAINING_TYPE_UID, result.getFirst().uid());
        assertEquals(TRAINING_TYPE_NAME, result.getFirst().name());
    }

    private static CreateTrainingRequest buildCreateTrainingRequest() {
        return CreateTrainingRequest.builder()
            .traineeUsername(TRAINEE_USERNAME)
            .trainerUsername(TRAINER_USERNAME)
            .name(TRAINING_NAME)
            .date(DATE)
            .durationInMinutes(DURATION_MINUTES)
            .build();
    }

    private static CreateTrainingDto buildCreateTrainingDto() {
        return CreateTrainingDto.builder()
            .traineeUsername(TRAINEE_USERNAME)
            .trainerUsername(TRAINER_USERNAME)
            .name(TRAINING_NAME)
            .date(DATE)
            .durationInMinutes(DURATION_MINUTES)
            .build();
    }

    private static GetTraineeTrainingsRequest buildTraineeTrainingsRequest() {
        return GetTraineeTrainingsRequest.builder()
            .username(TRAINEE_USERNAME)
            .from(FROM_DATE)
            .to(TO_DATE)
            .trainerUsername(TRAINER_USERNAME)
            .trainingTypeName(TRAINING_TYPE_NAME)
            .build();
    }

    private static TraineeTrainingsDto buildTraineeTrainingsDto() {
        return TraineeTrainingsDto.builder()
            .username(TRAINEE_USERNAME)
            .from(FROM_DATE)
            .to(TO_DATE)
            .trainerUsername(TRAINER_USERNAME)
            .trainingTypeName(TRAINING_TYPE_NAME)
            .build();
    }

    private static GetTrainerTrainingRequest buildTrainerTrainingsRequest() {
        return GetTrainerTrainingRequest.builder()
            .username(TRAINER_USERNAME)
            .from(FROM_DATE)
            .to(TO_DATE)
            .traineeUsername(TRAINEE_USERNAME)
            .build();
    }

    private static TrainerTrainingsDto buildTrainerTrainingsDto() {
        return TrainerTrainingsDto.builder()
            .username(TRAINER_USERNAME)
            .from(FROM_DATE)
            .to(TO_DATE)
            .traineeUsername(TRAINEE_USERNAME)
            .build();
    }

    private static Training buildTraining() {
        return Training.builder()
            .uid(UUID.randomUUID())
            .traineeUid(TRAINEE_UID)
            .trainerUid(TRAINER_UID)
            .name(TRAINING_NAME)
            .trainingType(buildTrainingType())
            .date(DATE)
            .duration(Duration.ofMinutes(DURATION_MINUTES))
            .build();
    }

    private static TrainingType buildTrainingType() {
        return TrainingType.builder()
            .uid(TRAINING_TYPE_UID)
            .name(TRAINING_TYPE_NAME)
            .build();
    }

    private static TrainingTypeResponse buildTrainingTypeResponse() {
        return TrainingTypeResponse.builder()
            .uid(TRAINING_TYPE_UID)
            .name(TRAINING_TYPE_NAME)
            .build();
    }

    private static Trainee buildTrainee() {
        return Trainee.builder()
            .uid(TRAINEE_UID)
            .username(TRAINEE_USERNAME)
            .build();
    }

    private static Trainer buildTrainer() {
        return Trainer.builder()
            .uid(TRAINER_UID)
            .username(TRAINER_USERNAME)
            .build();
    }
}
