package org.mouthaan.netify.common.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.mouthaan.namespace.netify.datatypes.genre.Genre;
import org.mouthaan.netify.service.dto.GenreDto;
import org.springframework.stereotype.Component;

@Component
public class GenreSoapMapper extends ConfigurableMapper {
    @Override
    protected void configure(MapperFactory factory) {

        factory.classMap(Genre.class, GenreDto.class)
                .byDefault()
                .register();
    }
}
