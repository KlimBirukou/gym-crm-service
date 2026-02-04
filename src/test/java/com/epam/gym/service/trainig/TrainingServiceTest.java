package com.epam.gym.service.trainig;

import com.epam.gym.domain.training.Training;
import com.epam.gym.mother.dto.training.CreateTrainingDtoMother;
import com.epam.gym.repository.training.ITrainingRepository;
import com.epam.gym.service.training.TrainingService;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import com.epam.gym.validator.IValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    private static final UUID TRAINEE_UID = UUID.randomUUID();
    private static final UUID TRAINER_UID = UUID.randomUUID();
    private static final String NAME = "name";
    private static final CreateTrainingDto CREATE_TRAINING_DTO =
        CreateTrainingDtoMother.get(TRAINEE_UID, TRAINER_UID, NAME, DATE);

    @Mock
    private ITrainingRepository trainingRepository;
    @Mock
    private IValidator<CreateTrainingDto> createTrainingValidator;

    @Captor
    private ArgumentCaptor<Training> trainingCaptor;

    @InjectMocks
    private TrainingService testObject;

    @Test
    void create_shouldCreateTraining_whenValidationPassed() {
        doNothing().when(createTrainingValidator)
            .validate(CREATE_TRAINING_DTO);

        var result = testObject.create(CREATE_TRAINING_DTO);
        verify(trainingRepository).save(trainingCaptor.capture());
        var saved = trainingCaptor.getValue();

        assertNotNull(result);
        assertNotNull(result.getUid());
        assertEquals(CREATE_TRAINING_DTO.traineeUid(), saved.getTraineeUid());
        assertEquals(CREATE_TRAINING_DTO.trainerUid(), saved.getTrainerUid());
        assertEquals(CREATE_TRAINING_DTO.date(), saved.getDate());
        assertEquals(CREATE_TRAINING_DTO.name(), saved.getName());
        assertEquals(CREATE_TRAINING_DTO.type(), saved.getType());
        assertEquals(CREATE_TRAINING_DTO.duration(), saved.getDuration());

        assertEquals(result.getUid(), saved.getUid());

        verify(createTrainingValidator, times(1))
            .validate(CREATE_TRAINING_DTO);
        verify(trainingRepository, times(1))
            .save(saved);
        verifyNoMoreInteractions(createTrainingValidator, trainingRepository);
    }

    @Test
    void create_shouldThrow_whenValidationFailed() {
        doThrow(new RuntimeException()).when(createTrainingValidator)
            .validate(CREATE_TRAINING_DTO);

        assertThrows(RuntimeException.class,
            () -> testObject.create(CREATE_TRAINING_DTO));

        verify(createTrainingValidator, times(1))
            .validate(CREATE_TRAINING_DTO);
        verifyNoInteractions(trainingRepository);
        verifyNoMoreInteractions(createTrainingValidator);
    }

    @ParameterizedTest
    @NullSource
    void create_shouldThrowException_whenDataNull(CreateTrainingDto dto) {
        assertThrows(NullPointerException.class,
            () -> testObject.create(dto));
    }
}
