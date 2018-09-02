package org.mouthaan.netify.service;

import org.mouthaan.netify.service.dto.GenreDto;
import org.mouthaan.netify.service.dto.MovieDto;
import org.mouthaan.netify.service.dto.RoleDto;

import java.util.List;

public interface MovieService extends BaseService<MovieDto, Integer>{
    MovieDto updateMovieAddGenre(Integer id, List<GenreDto> genres);
    MovieDto updateMovieRemoveGenre(Integer id, List<GenreDto> genres);
    MovieDto updateMovieAddCast(Integer id, List<RoleDto> cast);
    MovieDto updateMovieRemoveCast(Integer id, List<RoleDto> cast);
}
