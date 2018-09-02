package org.mouthaan.netify.service.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.mouthaan.netify.domain.model.Movie;
import org.mouthaan.netify.service.dto.MovieDto;
import org.springframework.stereotype.Component;

@Component
public class MovieServiceMapper extends ConfigurableMapper {
    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Movie.class, MovieDto.class)
//                .field("genres", "genres")
//                .field("cast", "cast")
                .mapNulls(false).mapNullsInReverse(false) // don't map nulls in both directions
                .byDefault()
                .register();
    }
}
