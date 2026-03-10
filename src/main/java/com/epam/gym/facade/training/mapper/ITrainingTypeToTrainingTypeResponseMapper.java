package com.epam.gym.facade.training.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.controller.rest.training.dto.response.TrainingTypeResponse;
import com.epam.gym.domain.training.TrainingType;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface ITrainingTypeToTrainingTypeResponseMapper
    extends Converter<@NonNull TrainingType, TrainingTypeResponse> {

    @Override
    TrainingTypeResponse convert(TrainingType source);
}
