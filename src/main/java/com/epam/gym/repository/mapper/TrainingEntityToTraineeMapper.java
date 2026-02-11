package com.epam.gym.repository.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.domain.training.Training;
import com.epam.gym.repository.jpa.entity.TrainingEntity;
import lombok.NonNull;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface TrainingEntityToTraineeMapper extends Converter<@NonNull TrainingEntity, Training> {

    @Override
    @Mapping(target = "traineeUid", source = "trainee.uid")
    @Mapping(target = "trainerUid", source = "trainer.uid")
    Training convert(TrainingEntity entity);

    @InheritInverseConfiguration
    @Mapping(target = "trainee.uid", source = "traineeUid")
    @Mapping(target = "trainer.uid", source = "trainerUid")
    TrainingEntity invertConverter(Training training);
}
