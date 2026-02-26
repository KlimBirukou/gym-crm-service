package com.epam.gym.facade.training.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.controlller.rest.training.dto.req.GetTraineeTrainingsRequest;
import com.epam.gym.service.training.dto.TraineeTrainingsDto;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface GetTraineeTrainingsRequestToTraineeTrainingsDtoMapper
    extends Converter<@NonNull GetTraineeTrainingsRequest, TraineeTrainingsDto> {

    @Override
    TraineeTrainingsDto convert(GetTraineeTrainingsRequest source);
}
