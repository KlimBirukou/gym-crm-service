package com.epam.gym.configuration;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.extensions.spring.SpringMapperConfig;

@MapperConfig(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = ConversionServiceAdapter.class
)
@SpringMapperConfig(
    conversionServiceAdapterPackage = "com.epam.gym.configuration"
)
public interface IMapStructConfiguration {
}
