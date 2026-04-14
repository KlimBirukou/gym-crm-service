package com.epam.gym.crm.facade.training.mapper;

import com.epam.gym.crm.configuration.IMapStructConfiguration;
import com.epam.gym.crm.controller.rest.training.dto.response.TrainingTypeResponse;
import com.epam.gym.crm.domain.training.TrainingType;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface ITrainingTypeToTrainingTypeResponseMapper
    extends Converter<@NonNull TrainingType, TrainingTypeResponse> {

    @Override
    TrainingTypeResponse convert(TrainingType source);
}
