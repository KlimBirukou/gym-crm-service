package com.epam.gym.repository.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.repository.entity.TrainingEntity;
import com.epam.gym.domain.training.Training;
import lombok.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class, uses = IDurationMapper.class)
public interface ITrainingEntityToTrainingMapper extends Converter<@NonNull TrainingEntity, Training> {

    @Override
    @Mapping(target = "traineeUid", source = "trainee.uid")
    @Mapping(target = "trainerUid", source = "trainer.uid")
    @Mapping(target = "duration", source = "duration", qualifiedByName = "fromMinutesToDuration")
    @Mapping(target = "trainingType", source = "trainingType")
    Training convert(@NonNull TrainingEntity entity);

    @Mapping(target = "trainee.uid", source = "traineeUid")
    @Mapping(target = "trainer.uid", source = "trainerUid")
    @Mapping(target = "duration", source = "duration", qualifiedByName = "fromDurationToMinutes")
    @Mapping(target = "trainingType", source = "trainingType")
    TrainingEntity invertConvert(@NonNull Training trainee);
}
