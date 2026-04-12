package com.epam.gym.crm.facade.training.mapper;

import com.epam.gym.crm.configuration.IMapStructConfiguration;
import com.epam.gym.crm.controller.rest.training.dto.request.GetTrainerTrainingRequest;
import com.epam.gym.crm.service.training.dto.TrainerTrainingsDto;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface GetTrainerTrainingsRequestToTrainerTrainingsDtoMapper
    extends Converter<@NonNull GetTrainerTrainingRequest, TrainerTrainingsDto> {

    @Override
    TrainerTrainingsDto convert(GetTrainerTrainingRequest source);
}
