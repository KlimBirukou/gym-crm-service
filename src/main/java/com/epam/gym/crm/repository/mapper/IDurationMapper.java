package com.epam.gym.crm.repository.mapper;

import com.epam.gym.crm.configuration.IMapStructConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.Duration;
import java.util.Optional;

@Mapper(config = IMapStructConfiguration.class)
public interface IDurationMapper {

    @Named("fromMinutesToDuration")
    default Duration toDuration(Integer minutes) {
        return Optional.ofNullable(minutes)
            .map(Duration::ofMinutes)
            .orElse(null);
    }

    @Named("fromDurationToMinutes")
    default Integer toMinutes(Duration duration) {
        return Optional.ofNullable(duration)
            .map(Duration::toMinutes)
            .map(Long::intValue)
            .orElse(null);
    }
}
