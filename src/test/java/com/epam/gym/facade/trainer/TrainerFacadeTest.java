package com.epam.gym.facade.trainer;

import com.epam.gym.controller.rest.trainer.dto.request.UpdateTrainerRequest;
import com.epam.gym.controller.rest.trainer.dto.response.TraineeProfileResponse;
import com.epam.gym.domain.training.TrainingType;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.service.assignment.IAssignmentService;
import com.epam.gym.service.trainer.ITrainerService;
import com.epam.gym.service.trainer.dto.UpdateTrainerDto;
import com.epam.gym.service.user.IUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TrainerFacadeTest {

    private static final UUID UID = UUID.randomUUID();
    private static final String FIRSTNAME = "firstname";
    private static final String NEW_FIRSTNAME = "new_firstname";
    private static final String LASTNAME = "lastname";
    private static final String NEW_LASTNAME = "new_lastname";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String SPECIALIZATION_NAME = "specialization";
    private static final String TRAINEE_USERNAME_1 = "trainee_username_1";
    private static final String TRAINEE_USERNAME_2 = "trainee_username_2";
    private static final LocalDate BIRTHDATE = LocalDate.of(2000, 1, 1);
    private static final String ADDRESS = "address";

    @Mock
    private IAssignmentService assignmentService;
    @Mock
    private ITrainerService trainerService;
    @Mock
    private ConversionService conversionService;
    @Mock
    private IUserService userService;

    @InjectMocks
    private TrainerFacade testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(assignmentService, trainerService, conversionService, userService);
    }

    @Test
    void getProfile_shouldReturnTrainerResponse_whenTrainerExists() {
        var trainer = buildTrainer();
        var trainee1 = buildTrainee(TRAINEE_USERNAME_1);
        var trainee2 = buildTrainee(TRAINEE_USERNAME_2);
        var traineeProfileResponse1 = buildTraineeProfileResponse(TRAINEE_USERNAME_1);
        var traineeProfileResponse2 = buildTraineeProfileResponse(TRAINEE_USERNAME_2);
        doReturn(trainer).when(trainerService).getByUsername(USERNAME);
        doReturn(List.of(trainee1, trainee2)).when(assignmentService).getTrainees(USERNAME, true, true);
        doReturn(traineeProfileResponse1).when(conversionService).convert(trainee1, TraineeProfileResponse.class);
        doReturn(traineeProfileResponse2).when(conversionService).convert(trainee2, TraineeProfileResponse.class);

        var result = testObject.getProfile(USERNAME);

        assertNotNull(result);
        assertEquals(USERNAME, result.username());
        assertEquals(FIRSTNAME, result.firstName());
        assertEquals(LASTNAME, result.lastName());
        assertEquals(SPECIALIZATION_NAME, result.specialization());
        assertTrue(result.active());
        assertEquals(2, result.trainees().size());
        assertEquals(TRAINEE_USERNAME_1, result.trainees().get(0).username());
        assertEquals(TRAINEE_USERNAME_2, result.trainees().get(1).username());
    }

    @ParameterizedTest
    @NullSource
    void getProfile_shouldThrowException_whenUsernameNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.getProfile(username));
    }


    @Test
    void updateTrainer_shouldUpdateAndReturnResponse_whenTrainerExists() {
        var updatedTrainer = buildUpdatedTrainer();
        var updateRequest = buildUpdateTrainerRequest();
        var trainee1 = buildTrainee(TRAINEE_USERNAME_1);
        var traineeProfileResponse1 = buildTraineeProfileResponse(TRAINEE_USERNAME_1);
        doReturn(updatedTrainer).when(trainerService).update(
            UpdateTrainerDto.builder()
                .username(USERNAME)
                .firstName(NEW_FIRSTNAME)
                .lastName(NEW_LASTNAME)
                .build()
        );
        doReturn(List.of(trainee1)).when(assignmentService).getTrainees(USERNAME, true, true);
        doReturn(traineeProfileResponse1).when(conversionService).convert(trainee1, TraineeProfileResponse.class);

        var result = testObject.updateTrainer(USERNAME, updateRequest);

        assertNotNull(result);
        assertEquals(USERNAME, result.username());
        assertEquals(NEW_FIRSTNAME, result.firstName());
        assertEquals(NEW_LASTNAME, result.lastName());
        assertEquals(SPECIALIZATION_NAME, result.specialization());
        assertTrue(result.active());
        assertEquals(1, result.trainees().size());
    }

    private static Stream<Arguments> provideUpdateTrainerNullArguments() {
        return Stream.of(
            Arguments.of(null, buildUpdateTrainerRequest()),
            Arguments.of(USERNAME, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideUpdateTrainerNullArguments")
    void updateTrainer_shouldThrowException_whenArgumentsNull(String username, UpdateTrainerRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.updateTrainer(username, request));
    }


    @Test
    void changeStatus_shouldCallUserService_whenAlways() {
        testObject.changeStatus(USERNAME, false);

        verify(userService).changeStatus(USERNAME, false);
    }

    @ParameterizedTest
    @NullSource
    void changeStatus_shouldThrowException_whenUsernameNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.changeStatus(username, false));
    }

    private static Trainer buildTrainer() {
        return Trainer.builder()
            .uid(UID)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .username(USERNAME)
            .password(PASSWORD)
            .specialization(buildTrainingType())
            .active(true)
            .build();
    }

    private static Trainer buildUpdatedTrainer() {
        return Trainer.builder()
            .uid(UID)
            .firstName(NEW_FIRSTNAME)
            .lastName(NEW_LASTNAME)
            .username(USERNAME)
            .password(PASSWORD)
            .specialization(buildTrainingType())
            .active(true)
            .build();
    }

    private static UpdateTrainerRequest buildUpdateTrainerRequest() {
        return UpdateTrainerRequest.builder()
            .firstName(NEW_FIRSTNAME)
            .lastName(NEW_LASTNAME)
            .specializationName(SPECIALIZATION_NAME)
            .build();
    }

    private static Trainee buildTrainee(String username) {
        return Trainee.builder()
            .uid(UUID.randomUUID())
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .username(username)
            .password(PASSWORD)
            .birthdate(BIRTHDATE)
            .address(ADDRESS)
            .active(true)
            .build();
    }

    private static TraineeProfileResponse buildTraineeProfileResponse(String username) {
        return TraineeProfileResponse.builder()
            .username(username)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .build();
    }

    private static TrainingType buildTrainingType() {
        return TrainingType.builder()
            .uid(UUID.randomUUID())
            .name(SPECIALIZATION_NAME)
            .build();
    }
}
