package org.mouthaan.netify.service.mapper;

import org.junit.jupiter.api.Test;
import org.mouthaan.netify.domain.model.Genre;
import org.mouthaan.netify.service.dto.GenreDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GenreServiceMapperTest {

    @Test
    void mapGenreToGenreDto() {

        // Given
        Genre genre = new Genre();
        genre.setId(2);
        genre.setName("Comedy");

        // when
        GenreDto genreDto = GenreServiceMapper.MAPPER.toGenreDto(genre);

        // then
        assertNotNull(genreDto);
        assertEquals(genre.getId(), genreDto.getId());
        assertEquals(genre.getName(), genreDto.getName());

    }

    @Test
    void mapGenreDtoToGenre() {

        // Given
        GenreDto genreDto = new GenreDto();
        genreDto.setId(2);
        genreDto.setName("Comedy");

        // when
        Genre genre = GenreServiceMapper.MAPPER.toGenre(genreDto);

        // then
        assertNotNull(genre);
        assertEquals(genreDto.getId(), genre.getId());
        assertEquals(genreDto.getName(), genre.getName());

    }

}