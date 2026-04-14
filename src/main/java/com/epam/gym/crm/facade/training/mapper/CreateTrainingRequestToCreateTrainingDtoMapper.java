package com.epam.gym.crm.facade.training.mapper;

import com.epam.gym.crm.configuration.IMapStructConfiguration;
import com.epam.gym.crm.controller.rest.training.dto.request.CreateTrainingRequest;
import com.epam.gym.crm.service.training.dto.CreateTrainingDto;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface CreateTrainingRequestToCreateTrainingDtoMapper
    extends Converter<@NonNull CreateTrainingRequest, CreateTrainingDto> {

    @Override
    CreateTrainingDto convert(CreateTrainingRequest source);
}
