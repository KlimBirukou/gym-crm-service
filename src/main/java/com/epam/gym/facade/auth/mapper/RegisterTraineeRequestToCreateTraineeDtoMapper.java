package com.epam.gym.facade.auth.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.controller.rest.auth.dto.request.RegisterTraineeRequest;
import com.epam.gym.service.trainee.dto.CreateTraineeDto;
import org.jspecify.annotations.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface RegisterTraineeRequestToCreateTraineeDtoMapper
    extends Converter<@NonNull RegisterTraineeRequest, CreateTraineeDto> {

    @Override
    CreateTraineeDto convert(@NonNull RegisterTraineeRequest source);
}
