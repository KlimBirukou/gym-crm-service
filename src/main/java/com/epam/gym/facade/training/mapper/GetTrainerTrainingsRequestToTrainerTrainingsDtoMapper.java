package com.epam.gym.facade.training.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.controller.rest.training.dto.request.GetTrainerTrainingRequest;
import com.epam.gym.service.training.dto.TrainerTrainingsDto;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface GetTrainerTrainingsRequestToTrainerTrainingsDtoMapper
    extends Converter<@NonNull GetTrainerTrainingRequest, TrainerTrainingsDto> {

    @Override
    TrainerTrainingsDto convert(GetTrainerTrainingRequest source);
}
