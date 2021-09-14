package com.example.mapper;

import com.example.dto.CityDto;
import com.example.model.City;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CityMapper {

    CityDto toDto(City entity);

    City toEntity(CityDto dto);

    City updateEntityFromDto(final CityDto dto, @MappingTarget City entity);
}
