package com.epam.gym.crm.facade.auth.mapper;

import com.epam.gym.crm.configuration.IMapStructConfiguration;
import com.epam.gym.crm.controller.rest.auth.dto.request.ChangePasswordRequest;
import com.epam.gym.crm.service.user.dto.ChangePasswordDto;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface ChangePasswordRequestToChangePasswordDtoMapper
    extends Converter<@NonNull ChangePasswordRequest, ChangePasswordDto> {

    @Override
    ChangePasswordDto convert(ChangePasswordRequest source);
}
