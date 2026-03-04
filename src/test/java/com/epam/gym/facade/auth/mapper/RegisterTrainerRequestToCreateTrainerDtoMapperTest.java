package com.epam.gym.facade.auth.mapper;

import com.epam.gym.controller.rest.auth.dto.request.RegisterTrainerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class RegisterTrainerRequestToCreateTrainerDtoMapperTest {

    private RegisterTrainerRequestToCreateTrainerDtoMapper testObject;

    @BeforeEach
    void setUp() {
        testObject = Mappers.getMapper(RegisterTrainerRequestToCreateTrainerDtoMapper.class);
    }

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of("firstname1", "lastname1", "fitness"),
            Arguments.of("firstname2", "lastname2", "fitness")
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapRequestToDto(String firstName, String lastName, String specialization) {
        var source = RegisterTrainerRequest.builder()
            .firstName(firstName)
            .lastName(lastName)
            .specialization(specialization)
            .build();

        var result = testObject.convert(source);

        assertNotNull(result);
        assertEquals(firstName, result.firstName());
        assertEquals(lastName, result.lastName());
        assertEquals(specialization, result.specialization());
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenSourceIsNull(RegisterTrainerRequest source) {
        assertNull(testObject.convert(source));
    }
}
