package com.epam.gym.facade.training.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.controlller.rest.training.dto.req.CreateTrainingRequest;
import com.epam.gym.service.training.dto.CreateTrainingDto;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface CreateTrainingRequestToCreateTrainingDtoMapper
    extends Converter<@NonNull CreateTrainingRequest, CreateTrainingDto> {

    @Override
    CreateTrainingDto convert(@NonNull CreateTrainingRequest source);
}
