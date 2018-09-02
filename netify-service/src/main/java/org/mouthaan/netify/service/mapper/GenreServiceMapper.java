package org.mouthaan.netify.service.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.mouthaan.netify.domain.model.Genre;
import org.mouthaan.netify.service.dto.GenreDto;
import org.springframework.stereotype.Component;

@Component
public class GenreServiceMapper extends ConfigurableMapper {

    @Override
    protected void configure(MapperFactory factory) {
        factory.classMap(Genre.class, GenreDto.class)
//                .field("id", "id")
//                .field("name", "name")
//                .field("gender", "gender")
                //.customize(customMapper)
                .mapNulls(false).mapNullsInReverse(false) // don't map nulls in both directions
                .byDefault()
                .register();
    }
}
