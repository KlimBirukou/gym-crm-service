package com.epam.gym.facade.auth.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.service.trainer.dto.CreateTrainerDto;
import org.jspecify.annotations.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface RegisterTrainerRequestToCreateTrainerDtoMapper
    extends Converter<@NonNull RegisterTrainerRequest, CreateTrainerDto> {

    @Override
    CreateTrainerDto convert(RegisterTrainerRequest request);
}
