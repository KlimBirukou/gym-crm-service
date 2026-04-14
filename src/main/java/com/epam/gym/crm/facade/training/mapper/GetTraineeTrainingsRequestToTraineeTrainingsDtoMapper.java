package com.epam.gym.crm.facade.training.mapper;

import com.epam.gym.crm.configuration.IMapStructConfiguration;
import com.epam.gym.crm.controller.rest.training.dto.request.GetTraineeTrainingsRequest;
import com.epam.gym.crm.service.training.dto.TraineeTrainingsDto;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface GetTraineeTrainingsRequestToTraineeTrainingsDtoMapper
    extends Converter<@NonNull GetTraineeTrainingsRequest, TraineeTrainingsDto> {

    @Override
    TraineeTrainingsDto convert(GetTraineeTrainingsRequest source);
}
