package com.epam.gym.configuration;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.extensions.spring.SpringMapperConfig;

@MapperConfig(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = ConversionServiceAdapter.class,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
@SpringMapperConfig(
    conversionServiceAdapterPackage = "com.epam.gym.configuration"
)
public interface IMapStructConfiguration {
}
