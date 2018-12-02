package org.mouthaan.netify.service.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.mouthaan.netify.domain.model.Genre;
import org.mouthaan.netify.service.dto.GenreDto;

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface GenreServiceMapper {

    GenreServiceMapper MAPPER = Mappers.getMapper(GenreServiceMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    GenreDto toGenreDto(Genre genre);

    @InheritInverseConfiguration
    Genre toGenre(GenreDto genreDto);
}
