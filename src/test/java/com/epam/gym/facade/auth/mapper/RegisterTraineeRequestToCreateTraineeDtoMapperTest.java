package com.epam.gym.facade.auth.mapper;

import com.epam.gym.controller.rest.auth.dto.request.RegisterTraineeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class RegisterTraineeRequestToCreateTraineeDtoMapperTest {

    private RegisterTraineeRequestToCreateTraineeDtoMapper testObject;

    @BeforeEach
    void setUp() {
        testObject = Mappers.getMapper(RegisterTraineeRequestToCreateTraineeDtoMapper.class);
    }

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of("firstname", "lastname", LocalDate.of(2000, 1, 1), "address"),
            Arguments.of("firstname", "lastname", null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapRequestToDto(String firstName, String lastName, LocalDate birthdate, String address) {
        var source = RegisterTraineeRequest.builder()
            .firstName(firstName)
            .lastName(lastName)
            .birthdate(birthdate)
            .address(address)
            .build();

        var result = testObject.convert(source);

        assertNotNull(result);
        assertEquals(firstName, result.firstName());
        assertEquals(lastName, result.lastName());
        assertEquals(birthdate, result.birthdate());
        assertEquals(address, result.address());
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenSourceIsNull(RegisterTraineeRequest source) {
        assertNull(testObject.convert(source));
    }
}
