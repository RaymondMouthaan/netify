package org.mouthaan.netify.common.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mouthaan.namespace.netify.datatypes.movie.Movie;
import org.mouthaan.netify.service.dto.MovieDto;

@Mapper
public interface MovieSoapMapper {

    MovieSoapMapper MAPPER = Mappers.getMapper(MovieSoapMapper.class);

    @Mapping(source = "genres.genre", target = "genres")
    @Mapping(source = "cast.role", target = "cast")
    MovieDto toMovieDto(Movie movie);

    @InheritInverseConfiguration
    Movie toMovie(MovieDto movieDto);
}
