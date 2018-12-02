package org.mouthaan.netify.service.impl;

import org.mouthaan.netify.domain.dao.BaseDao;
import org.mouthaan.netify.domain.dao.SearchCriteria;
import org.mouthaan.netify.domain.model.Genre;
import org.mouthaan.netify.domain.repository.GenreRepository;
import org.mouthaan.netify.service.GenreService;
import org.mouthaan.netify.service.dto.CountDto;
import org.mouthaan.netify.service.dto.GenreDto;
import org.mouthaan.netify.service.exception.AlreadyExistException;
import org.mouthaan.netify.service.exception.NotFoundException;
import org.mouthaan.netify.service.mapper.GenreServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("genreService")
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final BaseDao baseDAO;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository, BaseDao baseDAO) {
        this.genreRepository = genreRepository;
        this.baseDAO = baseDAO;
    }

    @Override
    public CountDto countAll() {
        return new CountDto(this.genreRepository.count());
    }

    @Override
    public List<GenreDto> findAll(Map<String,String> filterParams) {

        List<SearchCriteria> queryParams = new ArrayList<>();
        filterParams.forEach((k,v)->{
            queryParams.add( new SearchCriteria(k, "%", v));
        });

        List<GenreDto> genreDtoS = this.baseDAO.findAllPredicated(queryParams, Genre.class).stream()
                .map(GenreServiceMapper.MAPPER::toGenreDto)
                .collect(Collectors.toList());
        if(genreDtoS.size()==0){
            String filter = queryParams.stream().map(SearchCriteria::toString)
                    .collect(Collectors.joining("; "));
            throw new NotFoundException("element.type.genre", filter);
        }
        return genreDtoS;
    }

    @Override
    public Page<GenreDto> findPaginated(Pageable pageable) {
        return this.genreRepository.findAll(pageable).map(GenreServiceMapper.MAPPER::toGenreDto);
    }

    @Override
    public GenreDto findById(Integer id) {
        Genre genre = this.genreRepository.getOne(id);
        if (null == genre) {
            throw new NotFoundException("element.type.genre", "id=\'" + id + "\'");
        }
        return GenreServiceMapper.MAPPER.toGenreDto(genre);
    }

    @Override
    public GenreDto findByName(String name) {
        Genre genre = this.genreRepository.findByName(name);
        if (null == genre) {
            throw new NotFoundException("element.type.genre", "name=\'" + name + "\'");
        }
        return GenreServiceMapper.MAPPER.toGenreDto(genre);
    }

    @Override
    public GenreDto add(GenreDto genreDto) {
        if (this.isExists(genreDto)) {
            throw new AlreadyExistException("element.type.genre", genreDto.getName());
        }
        return GenreServiceMapper.MAPPER.toGenreDto(genreRepository.saveAndFlush(GenreServiceMapper.MAPPER.toGenre(genreDto)));
    }

    @Override
    public GenreDto update(Integer id, GenreDto genreDto) {
        Optional<Genre> genre = this.genreRepository.findById(id);

        if (genre.isPresent()) {
            genre.get().setName(genreDto.getName());
            return GenreServiceMapper.MAPPER.toGenreDto(genreRepository.saveAndFlush(genre.get()));
        } else {
            throw new NotFoundException("element.type.genre", "id=\'" + id + "\'");
        }
    }

    @Override
    public void delete(Integer id) {
        Optional<Genre> genre = this.genreRepository.findById(id);

        if (genre.isPresent()) {
            this.genreRepository.delete(genre.get());
        } else {
            throw new NotFoundException("element.type.genre", "id=\'" + id + "\'");
        }
    }

    @Override
    public boolean isExists(GenreDto genre) {
        return this.genreRepository.findByName(genre.getName()) != null;
    }
}
