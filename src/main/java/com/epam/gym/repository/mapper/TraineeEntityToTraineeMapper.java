package com.epam.gym.repository.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import com.epam.gym.domain.user.Trainee;
import com.epam.gym.repository.jpa.user.trainee.TraineeEntity;
import lombok.NonNull;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.extensions.spring.DelegatingConverter;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = IMapStructConfiguration.class)
public interface TraineeEntityToTraineeMapper extends Converter<@NonNull TraineeEntity, Trainee> {

    @Override
    @Mapping(source = "user", target = ".")
    Trainee convert(TraineeEntity entity);

    @InheritInverseConfiguration
    @DelegatingConverter
    @Mapping(source = ".", target = "user")
    TraineeEntity invertConvert(Trainee trainer);
}
