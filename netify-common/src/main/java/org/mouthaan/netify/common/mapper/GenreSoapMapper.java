package org.mouthaan.netify.common.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mouthaan.namespace.netify.datatypes.genre.Genre;
import org.mouthaan.netify.service.dto.GenreDto;

@Mapper
public interface GenreSoapMapper {

    GenreSoapMapper MAPPER = Mappers.getMapper(GenreSoapMapper.class);

    GenreDto toGenreDto(Genre genre);

    Genre toGenre(GenreDto genreDto);
}
