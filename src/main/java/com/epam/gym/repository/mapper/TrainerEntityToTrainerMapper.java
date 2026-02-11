package com.epam.gym.repository.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.domain.user.Trainer;
import com.epam.gym.repository.jpa.entity.TrainerEntity;
import lombok.NonNull;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface TrainerEntityToTrainerMapper extends Converter<@NonNull TrainerEntity, Trainer> {

    @Override
    Trainer convert(TrainerEntity entity);

    @InheritInverseConfiguration
    @DelegatingConverter
    TrainerEntity invertConvert(Trainer trainer);
}
