package org.mouthaan.netify.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mouthaan.netify.domain.model.Movie;
import org.mouthaan.netify.service.dto.MovieDto;

@Mapper
public interface MovieServiceMapper {

    MovieServiceMapper MAPPER = Mappers.getMapper(MovieServiceMapper.class);

    MovieDto toMovieDto(Movie movie);

    Movie toMovie(MovieDto movieDto);
}
