package com.epam.gym.crm.facade.auth.mapper;

import com.epam.gym.crm.configuration.IMapStructConfiguration;
import com.epam.gym.crm.controller.rest.auth.dto.request.RegisterTrainerRequest;
import com.epam.gym.crm.service.trainer.dto.CreateTrainerDto;
import org.jspecify.annotations.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface RegisterTrainerRequestToCreateTrainerDtoMapper
    extends Converter<@NonNull RegisterTrainerRequest, CreateTrainerDto> {

    @Override
    CreateTrainerDto convert(RegisterTrainerRequest request);
}
