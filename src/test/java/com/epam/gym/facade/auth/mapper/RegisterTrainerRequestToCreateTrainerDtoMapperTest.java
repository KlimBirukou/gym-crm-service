package com.epam.gym.facade.auth.mapper;

import com.epam.gym.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RegisterTrainerRequestToCreateTrainerDtoMapperTest {

    private final RegisterTrainerRequestToCreateTrainerDtoMapper testObject =
        Mappers.getMapper(RegisterTrainerRequestToCreateTrainerDtoMapper.class);

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

        var actual = testObject.convert(source);

        var expected = new CreateTrainerDto(firstName, lastName, specialization);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenSourceIsNull(RegisterTrainerRequest source) {
        assertNull(testObject.convert(source));
    }
}
