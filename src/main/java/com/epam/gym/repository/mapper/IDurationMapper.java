package com.epam.gym.repository.mapper;

import com.epam.gym.configuration.IMapStructConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.Duration;
import java.util.Objects;

@Mapper(config = IMapStructConfiguration.class)
public interface IDurationMapper {

    @Named("fromMinutesToDuration")
    default Duration toDuration(Integer minutes) {
        return Objects.isNull(minutes)
            ? null
            : Duration.ofMinutes(minutes);
    }

    @Named("fromDurationToMinutes")
    default Integer toMinutes(Duration duration) {
        return Objects.isNull(duration)
            ? null
            : (int) duration.toMinutes();
    }
}
