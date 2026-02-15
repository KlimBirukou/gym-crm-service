package com.epam.gym.repository.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.repository.entity.TrainingTypeEntity;
import com.epam.gym.domain.training.TrainingType;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface ITrainingTypeEntityToTrainingTypeMapper extends Converter<@NonNull TrainingTypeEntity, TrainingType> {

    @Override
    TrainingType convert(@NonNull TrainingTypeEntity entity);

    TrainingTypeEntity invertConvert(@NonNull TrainingType type);
}
