package org.mouthaan.netify.common.soap;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mouthaan.namespace.netify.datatypes.genre.Genre;
import org.mouthaan.namespace.netify.datatypes.genre.Genres;
import org.mouthaan.namespace.netify.general.*;
import org.mouthaan.netify.common.mapper.GenreSoapMapper;
import org.mouthaan.netify.service.GenreService;
import org.mouthaan.netify.service.dto.CountDto;
import org.mouthaan.netify.service.dto.GenreDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class GenreFacade {
    private final GenreService genreService;
    private final GenreSoapMapper genreSoapMapper;

    public GetGenreCountResponse getSoapGenreCount() {
        CountDto countDto = genreService.countAll();
        GetGenreCountResponse getGenreCountResponse = new GetGenreCountResponse();
        getGenreCountResponse.setCount(countDto.getCount());
        return getGenreCountResponse;
    }

    public GetGenreAllResponse getSoapGenreAll(GetGenreAllRequest getGenreAllRequest) {
        Map<String,String> filterParams = new HashMap();
        if (null != getGenreAllRequest.getFilters()
                && null != getGenreAllRequest.getFilters().getName()){
            filterParams.put("name",getGenreAllRequest.getFilters().getName());
        }

        List<GenreDto> genreDtos = this.genreService.findAll(filterParams);

        GetGenreAllResponse getGenreAllResponse = new GetGenreAllResponse();
        getGenreAllResponse.setGenres(new Genres());
        genreDtos.forEach(genreDto -> {
            // Add genreDto to response
            getGenreAllResponse.getGenres().getGenre().add(genreSoapMapper.map(genreDto, Genre.class));
        });
        return getGenreAllResponse;
    }

    public GetGenreByIdResponse getSoapGenreById(GetGenreByIdRequest getGenreByIdRequest) {
        // Find genre by id
        GenreDto genreDto = genreService.findById(getGenreByIdRequest.getId());

        // Add genreDto to response
        GetGenreByIdResponse getGenreByIdResponse = new GetGenreByIdResponse();
        getGenreByIdResponse.setGenre(genreSoapMapper.map(genreDto, Genre.class));
        return getGenreByIdResponse;
    }

    public AddGenreResponse addSoapGenre(AddGenreRequest addGenreRequest) {
        GenreDto genreDto = genreSoapMapper.map(addGenreRequest.getGenre(), GenreDto.class);

        // Add genreDto to addGenreResponse
        AddGenreResponse addGenreResponse = new AddGenreResponse();
        addGenreResponse.setGenre(genreSoapMapper.map(genreService.add(genreDto), Genre.class));
        return addGenreResponse;
    }

    public UpdateGenreResponse updateSoapGenre(UpdateGenreRequest updateGenreRequest) {
        GenreDto genreDto = genreService.update(updateGenreRequest.getGenre().getId(),
                genreSoapMapper.map(updateGenreRequest.getGenre(), GenreDto.class));

        // Add genreDto to response
        UpdateGenreResponse updateGenreResponse = new UpdateGenreResponse();
        updateGenreResponse.setGenre(genreSoapMapper.map(genreDto, Genre.class));
        return updateGenreResponse;
    }

    public DeleteGenreResponse deleteSoapGenre(DeleteGenreRequest deleteGenreRequest) {
        genreService.delete(deleteGenreRequest.getId());

        DeleteGenreResponse deleteGenreResponse = new DeleteGenreResponse();
        Genre genre = new Genre();
        genre.setId(deleteGenreRequest.getId());
        deleteGenreResponse.setGenre(genre);
        return deleteGenreResponse;
    }
}
