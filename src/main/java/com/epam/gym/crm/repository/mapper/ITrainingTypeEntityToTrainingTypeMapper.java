package com.epam.gym.crm.repository.mapper;

import com.epam.gym.crm.configuration.IMapStructConfiguration;
import com.epam.gym.crm.repository.entity.TrainingTypeEntity;
import com.epam.gym.crm.domain.training.TrainingType;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface ITrainingTypeEntityToTrainingTypeMapper extends Converter<@NonNull TrainingTypeEntity, TrainingType> {

    @Override
    TrainingType convert(TrainingTypeEntity entity);
}
