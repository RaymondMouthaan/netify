package org.mouthaan.netify.common.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.mouthaan.namespace.netify.datatypes.movie.Movie;
import org.mouthaan.netify.service.dto.MovieDto;
import org.springframework.stereotype.Component;

@Component
public class MovieSoapMapper extends ConfigurableMapper {
    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Movie.class, MovieDto.class)
                .field("genres.genre", "genres")
                .field("cast.role", "cast")
                .byDefault()
                .register();
    }
}
