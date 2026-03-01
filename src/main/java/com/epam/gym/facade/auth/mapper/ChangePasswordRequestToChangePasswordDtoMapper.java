package com.epam.gym.facade.auth.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.controller.rest.auth.dto.request.ChangePasswordRequest;
import com.epam.gym.service.user.dto.ChangePasswordDto;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface ChangePasswordRequestToChangePasswordDtoMapper
    extends Converter<@NonNull ChangePasswordRequest, ChangePasswordDto> {

    ChangePasswordDto convert(@NonNull ChangePasswordRequest source);
}
