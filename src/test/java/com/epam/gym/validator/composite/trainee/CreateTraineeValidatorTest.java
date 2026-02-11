package com.epam.gym.validator.composite.trainee;

import com.epam.gym.mother.dto.trainee.CreateTraineeDtoMother;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import com.epam.gym.validator.IValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateTraineeValidatorTest {

    private static final LocalDate DATE = LocalDate.of(2026, 1, 1);
    public static final String FIRSTNAME = "firstName";
    public static final String LASTNAME = "lastName";
    public static final String ADDRESS = "address";
    private static final CreateTraineeDto CREATE_TRAINEE_DTO =
        CreateTraineeDtoMother.get(FIRSTNAME, LASTNAME, ADDRESS, DATE);

    @Mock
    private IValidator<LocalDate> traineeBirthdateValidator;

    private CreateTraineeValidator testObject;

    @BeforeEach
    void setUp() {
        testObject = new CreateTraineeValidator(
            traineeBirthdateValidator
        );
        testObject.setUpValidator();
    }

    @Test
    void validate_shouldPass_whenAllValidatorsSuccessful() {
        doNothing().when(traineeBirthdateValidator).validate(DATE);

        assertDoesNotThrow(() -> testObject.validate(CREATE_TRAINEE_DTO));

        verify(traineeBirthdateValidator, times(1))
            .validate(DATE);
    }

    @Test
    void validate_shouldThrow_whenTraineeBirthdateValidatorFailed() {
        doThrow(new RuntimeException()).when(traineeBirthdateValidator)
            .validate(DATE);

        assertThrows(RuntimeException.class,
            () -> testObject.validate(CREATE_TRAINEE_DTO));
    }

    @ParameterizedTest
    @NullSource
    void validate_shouldThrowException_whenDataNull(CreateTraineeDto dto) {
        assertThrows(NullPointerException.class,
            () -> testObject.validate(dto));
    }
}
