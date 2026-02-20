package com.epam.gym.facade;

import com.epam.gym.domain.user.Trainee;
import com.epam.gym.service.trainee.ITraineeService;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class GymFacadeTest {

    private static final LocalDate DATE = LocalDate.of(2000, 1, 1);
    private static final UUID UID = UUID.randomUUID();
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String USERNAME = "username";
    private static final String HASHED_PASSWORD = "hashed_password";
    private static final String ADDRESS = "address";

    @Mock
    private ITraineeService traineeService;

    @InjectMocks
    private GymFacade testObject;

    @Test
    void createTrainee_shouldReturnTrainee() {
        var dto = getCreateTraineeDto();
        var trainee = getTrainee();
        doReturn(trainee).when(traineeService).create(dto);

        var result = testObject.createTrainee(dto);

        assertEquals(trainee, result);
        verify(traineeService, times(1)).create(dto);
        verifyNoMoreInteractions(traineeService);
    }

    @ParameterizedTest
    @NullSource
    void createTrainee_shouldThrowException_whenArgumentNull(CreateTraineeDto dto) {
        assertThrows(NullPointerException.class, () -> testObject.createTrainee(dto));

        verifyNoMoreInteractions(traineeService);
    }

    private static CreateTraineeDto getCreateTraineeDto() {
        return new CreateTraineeDto(
            FIRSTNAME, LASTNAME, DATE, ADDRESS
        );
    }

    private static Trainee getTrainee() {
        return Trainee.builder()
            .uid(UID)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .username(USERNAME)
            .password(HASHED_PASSWORD)
            .birthdate(DATE)
            .address(ADDRESS)
            .active(true)
            .build();
    }
}