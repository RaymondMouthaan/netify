package org.mouthaan.netify.service;


import org.mouthaan.netify.service.dto.GenreDto;

public interface GenreService extends BaseService<GenreDto, Integer> {
    GenreDto findByName(String name);
}
