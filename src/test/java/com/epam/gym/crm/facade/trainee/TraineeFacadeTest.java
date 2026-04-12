package com.epam.gym.crm.facade.trainee;

import com.epam.gym.crm.controller.rest.trainee.dto.request.UpdateTraineeRequest;
import com.epam.gym.crm.controller.rest.trainee.dto.response.TrainerProfileResponse;
import com.epam.gym.crm.domain.training.TrainingType;
import com.epam.gym.crm.domain.user.Trainee;
import com.epam.gym.crm.domain.user.Trainer;
import com.epam.gym.crm.service.assignment.IAssignmentService;
import com.epam.gym.crm.service.trainee.TraineeService;
import com.epam.gym.crm.service.trainee.dto.UpdateTraineeDto;
import com.epam.gym.crm.service.user.IUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TraineeFacadeTest {

    private static final UUID UID = UUID.randomUUID();
    private static final String FIRSTNAME = "firstname";
    private static final String NEW_FIRSTNAME = "new_firstname";
    private static final String LASTNAME = "lastname";
    private static final String NEW_LASTNAME = "new_lastname";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ADDRESS = "address";
    private static final String NEW_ADDRESS = "new_address";
    private static final LocalDate BIRTHDATE = LocalDate.of(2000, 1, 1);
    private static final LocalDate NEW_BIRTHDATE = LocalDate.of(1995, 6, 15);
    private static final String TRAINER_USERNAME_1 = "trainer_username_1";
    private static final String TRAINER_USERNAME_2 = "trainer_username_2";
    private static final String SPECIALIZATION_NAME = "specialization";

    @Mock
    private IAssignmentService assignmentService;
    @Mock
    private TraineeService traineeService;
    @Mock
    private ConversionService conversionService;
    @Mock
    private IUserService userService;

    @InjectMocks
    private TraineeFacade testObject;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(assignmentService, traineeService, conversionService, userService);
    }

    @Test
    void getProfile_shouldReturnTraineeResponse_whenTraineeExists() {
        var trainee = buildTrainee();
        var trainer1 = buildTrainer(TRAINER_USERNAME_1);
        var trainer2 = buildTrainer(TRAINER_USERNAME_2);
        var trainerProfileResponse1 = buildTrainerProfileResponse(TRAINER_USERNAME_1);
        var trainerProfileResponse2 = buildTrainerProfileResponse(TRAINER_USERNAME_2);
        doReturn(trainee).when(traineeService).getByUsername(USERNAME);
        doReturn(List.of(trainer1, trainer2)).when(assignmentService).getTrainers(USERNAME, true, true);
        doReturn(trainerProfileResponse1).when(conversionService).convert(trainer1, TrainerProfileResponse.class);
        doReturn(trainerProfileResponse2).when(conversionService).convert(trainer2, TrainerProfileResponse.class);

        var result = testObject.getProfile(USERNAME);

        assertNotNull(result);
        assertEquals(USERNAME, result.username());
        assertEquals(FIRSTNAME, result.firstName());
        assertEquals(LASTNAME, result.lastName());
        assertEquals(BIRTHDATE, result.birthdate());
        assertEquals(ADDRESS, result.address());
        assertTrue(result.active());
        assertEquals(2, result.trainers().size());
        assertEquals(TRAINER_USERNAME_1, result.trainers().get(0).username());
        assertEquals(TRAINER_USERNAME_2, result.trainers().get(1).username());
    }

    @ParameterizedTest
    @NullSource
    void getProfile_shouldThrowException_whenUsernameNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.getProfile(username));
    }

    @Test
    void updateTrainee_shouldUpdateAndReturnResponse_whenTraineeExists() {
        var updatedTrainee = buildUpdatedTrainee();
        var updateRequest = buildUpdateTraineeRequest();
        var trainer1 = buildTrainer(TRAINER_USERNAME_1);
        var trainerProfileResponse1 = buildTrainerProfileResponse(TRAINER_USERNAME_1);
        doReturn(updatedTrainee).when(traineeService).update(
            UpdateTraineeDto.builder()
                .username(USERNAME)
                .firstName(NEW_FIRSTNAME)
                .lastName(NEW_LASTNAME)
                .birthdate(NEW_BIRTHDATE)
                .address(NEW_ADDRESS)
                .build()
        );
        doReturn(List.of(trainer1)).when(assignmentService).getTrainers(USERNAME, true, true);
        doReturn(trainerProfileResponse1).when(conversionService).convert(trainer1, TrainerProfileResponse.class);

        var result = testObject.updateTrainee(USERNAME, updateRequest);

        assertNotNull(result);
        assertEquals(USERNAME, result.username());
        assertEquals(NEW_FIRSTNAME, result.firstName());
        assertEquals(NEW_LASTNAME, result.lastName());
        assertEquals(NEW_BIRTHDATE, result.birthdate());
        assertEquals(NEW_ADDRESS, result.address());
        assertTrue(result.active());
        assertEquals(1, result.trainers().size());
    }

    @ParameterizedTest
    @NullSource
    void updateTrainee_shouldThrowException_whenUsernameNull(String username) {
        var updateRequest = buildUpdateTraineeRequest();

        assertThrows(NullPointerException.class, () -> testObject.updateTrainee(username, updateRequest));
    }

    @ParameterizedTest
    @NullSource
    void updateTrainee_shouldThrowException_whenRequestNull(UpdateTraineeRequest request) {
        assertThrows(NullPointerException.class, () -> testObject.updateTrainee(USERNAME, request));
    }

    @Test
    void changeStatus_shouldCallUserService_whenAlways() {
        testObject.changeStatus(USERNAME, true);

        verify(userService).changeStatus(USERNAME, true);
    }

    @ParameterizedTest
    @NullSource
    void changeStatus_shouldThrowException_whenUsernameNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.changeStatus(username, true));
    }

    @Test
    void delete_shouldCallTraineeService_whenAlways() {
        testObject.delete(USERNAME);

        verify(traineeService).delete(USERNAME);
    }

    @ParameterizedTest
    @NullSource
    void delete_shouldThrowException_whenUsernameNull(String username) {
        assertThrows(NullPointerException.class, () -> testObject.delete(username));
    }

    private static Trainee buildTrainee() {
        return Trainee.builder()
            .uid(UID)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .username(USERNAME)
            .password(PASSWORD)
            .birthdate(BIRTHDATE)
            .address(ADDRESS)
            .active(true)
            .build();
    }

    private static Trainee buildUpdatedTrainee() {
        return Trainee.builder()
            .uid(UID)
            .firstName(NEW_FIRSTNAME)
            .lastName(NEW_LASTNAME)
            .username(USERNAME)
            .password(PASSWORD)
            .birthdate(NEW_BIRTHDATE)
            .address(NEW_ADDRESS)
            .active(true)
            .build();
    }

    private static UpdateTraineeRequest buildUpdateTraineeRequest() {
        return UpdateTraineeRequest.builder()
            .firstName(NEW_FIRSTNAME)
            .lastName(NEW_LASTNAME)
            .birthdate(NEW_BIRTHDATE)
            .address(NEW_ADDRESS)
            .build();
    }

    private static Trainer buildTrainer(String username) {
        return Trainer.builder()
            .uid(UUID.randomUUID())
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .username(username)
            .password(PASSWORD)
            .specialization(buildTrainingType())
            .active(true)
            .build();
    }

    private static TrainerProfileResponse buildTrainerProfileResponse(String username) {
        return TrainerProfileResponse.builder()
            .username(username)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .specializationName(SPECIALIZATION_NAME)
            .build();
    }

    private static TrainingType buildTrainingType() {
        return TrainingType.builder()
            .uid(UUID.randomUUID())
            .name(SPECIALIZATION_NAME)
            .build();
    }
}
