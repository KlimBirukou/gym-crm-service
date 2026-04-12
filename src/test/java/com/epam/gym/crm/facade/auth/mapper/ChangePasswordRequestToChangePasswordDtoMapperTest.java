package com.epam.gym.crm.facade.auth.mapper;

import com.epam.gym.crm.controller.rest.auth.dto.request.ChangePasswordRequest;
import com.epam.gym.crm.service.user.dto.ChangePasswordDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mapstruct.factory.Mappers;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ChangePasswordRequestToChangePasswordDtoMapperTest {

    private static final ChangePasswordRequestToChangePasswordDtoMapper testObject =
        Mappers.getMapper(ChangePasswordRequestToChangePasswordDtoMapper.class);

    private static Stream<Arguments> provideConvertData() {
        return Stream.of(
            Arguments.of("username1", "oldPassword1", "newPassword1"),
            Arguments.of("username2", "oldPassword2", "newPassword2")
        );
    }

    @ParameterizedTest
    @MethodSource("provideConvertData")
    void convert_shouldMapRequestToDto(String username, String oldPassword, String newPassword) {
        var source = ChangePasswordRequest.builder()
            .username(username)
            .oldPassword(oldPassword)
            .newPassword(newPassword)
            .build();

        var result = testObject.convert(source);

        var expectedChangePasswordDto = ChangePasswordDto.builder()
            .username(username)
            .newPassword(newPassword)
            .oldPassword(oldPassword)
            .build();
        assertEquals(expectedChangePasswordDto, result);
    }

    @ParameterizedTest
    @NullSource
    void convert_shouldReturnNull_whenSourceIsNull(ChangePasswordRequest source) {
        assertNull(testObject.convert(source));
    }
}
