package org.mouthaan.netify.common.soap;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mouthaan.namespace.netify.datatypes.movie.Movie;
import org.mouthaan.namespace.netify.datatypes.movie.Movies;
import org.mouthaan.namespace.netify.general.*;
import org.mouthaan.netify.common.mapper.GenreSoapMapper;
import org.mouthaan.netify.common.mapper.MovieSoapMapper;
import org.mouthaan.netify.common.mapper.RoleSoapMapper;
import org.mouthaan.netify.domain.dao.SearchCriteria;
import org.mouthaan.netify.service.MovieService;
import org.mouthaan.netify.service.dto.CountDto;
import org.mouthaan.netify.service.dto.GenreDto;
import org.mouthaan.netify.service.dto.MovieDto;
import org.mouthaan.netify.service.dto.RoleDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@AllArgsConstructor
public class MovieFacade {

    private final MovieService movieService;

    public GetMovieCountResponse getSoapMovieCount() {
        CountDto countDto = movieService.countAll();
        GetMovieCountResponse getMovieCountResponse = new GetMovieCountResponse();
        getMovieCountResponse.setCount(countDto.getCount());
        return getMovieCountResponse;
    }

    public GetMovieAllResponse getSoapMovieAll(GetMovieAllRequest getMovieAllRequest) {
        List<SearchCriteria> queryParams = new ArrayList<>();
        Map<String,String> filterParams = new HashMap<>();
        if (null != getMovieAllRequest.getFilters()) {
            if (getMovieAllRequest.getFilters().getTitle() != null)
                filterParams.put("title",getMovieAllRequest.getFilters().getTitle());
            if (getMovieAllRequest.getFilters().getMinYear() != null)
                filterParams.put("min_year",getMovieAllRequest.getFilters().getMinYear().toString());
            if (getMovieAllRequest.getFilters().getMinYear() != null)
                filterParams.put("max_year",getMovieAllRequest.getFilters().getMaxYear().toString());
            if (getMovieAllRequest.getFilters().getMinYear() != null)
                filterParams.put("min_runtime",getMovieAllRequest.getFilters().getMinRuntime().toString());
            if (getMovieAllRequest.getFilters().getMinYear() != null)
                filterParams.put("max_runtime",getMovieAllRequest.getFilters().getMaxRuntime().toString());
            if (getMovieAllRequest.getFilters().getMinYear() != null)
                filterParams.put("min_popularity",getMovieAllRequest.getFilters().getMinPopularity().toString());
            if (getMovieAllRequest.getFilters().getMinYear() != null)
                filterParams.put("max_popularity",getMovieAllRequest.getFilters().getMaxPopularity().toString());
        }

        // Find all movies
        List<MovieDto> movieDtoS = movieService.findAll(filterParams);

        // Create getMovieAllResponse
        GetMovieAllResponse getMovieAllResponse = new GetMovieAllResponse();
        getMovieAllResponse.setMovies(new Movies());

        // Map each movieDto to getMovieAllResponse
        movieDtoS.forEach(movieDto -> {
            // Add movieDto to getMovieAllResponse
            getMovieAllResponse.getMovies().getMovie().add(MovieSoapMapper.MAPPER.toMovie(movieDto));
        });

        // return getMovieAllResponse
        return getMovieAllResponse;
    }

    public GetMovieByIdResponse getSoapMovieById(GetMovieByIdRequest getMovieByIdRequest) {
        // Find movie by id
        MovieDto movieDto = movieService.findById(getMovieByIdRequest.getId());

        // Create getMovieByIdResponse
        GetMovieByIdResponse getMovieByIdResponse = new GetMovieByIdResponse();
        // Map movieDto to Movie and set Movie to getMovieByIdResponse
        getMovieByIdResponse.setMovie(MovieSoapMapper.MAPPER.toMovie(movieDto));

        // Return getMovieByIdResponse
        return getMovieByIdResponse;
    }

    public AddMovieResponse addSoapMovie(AddMovieRequest addMovieRequest) {
        // Map incoming movie request to movieDto
        MovieDto movieDto = MovieSoapMapper.MAPPER.toMovieDto(addMovieRequest.getMovie());

        // Add movieDto
        MovieDto movieResponseDto = movieService.add(movieDto);

        // Map movieResponseDto to movieResponse
        Movie movieResponse = MovieSoapMapper.MAPPER.toMovie(movieResponseDto);

        // Add movieResponse to addMovieResponse
        AddMovieResponse addMovieResponse = new AddMovieResponse();
        addMovieResponse.setMovie(movieResponse);

        // return addMovieResponse
        return addMovieResponse;
    }

    public UpdateMovieResponse updateSoapMovie(UpdateMovieRequest updateMovieRequest) {
        MovieDto movieDto = movieService.update(updateMovieRequest.getMovie().getId(), MovieSoapMapper.MAPPER.toMovieDto(updateMovieRequest.getMovie()));

        // Add movieDto to response
        UpdateMovieResponse updateMovieResponse = new UpdateMovieResponse();
        updateMovieResponse.setMovie(MovieSoapMapper.MAPPER.toMovie(movieDto));
        return updateMovieResponse;
    }

    public UpdateMovieAddGenreResponse updateSoapMovieAddGenre(UpdateMovieAddGenreRequest updateMovieAddGenreRequest) {
        // Get movie id to update
        Integer movieId = updateMovieAddGenreRequest.getMovieId();

        // Get genres to add
        List<GenreDto> genreDtos = new ArrayList<>();
        updateMovieAddGenreRequest.getGenre().forEach(genre -> genreDtos.add(GenreSoapMapper.MAPPER.toGenreDto(genre)));

        // Update movie with genres to add
        MovieDto movieResponseDto = movieService.updateMovieAddGenre(movieId, genreDtos);

        // Map movieResponseDto to movieResponse
        Movie movieResponse = MovieSoapMapper.MAPPER.toMovie(movieResponseDto);

        // Create updateMovieAddGenreResponse with movieResponse
        UpdateMovieAddGenreResponse updateMovieAddGenreResponse = new UpdateMovieAddGenreResponse();
        updateMovieAddGenreResponse.setMovie(movieResponse);

        // Return updateMovieAddGenreResponse
        return updateMovieAddGenreResponse;
    }

    public UpdateMovieRemoveGenreResponse updateSoapMovieRemoveGenre(UpdateMovieRemoveGenreRequest updateMovieRemoveGenreRequest) {
        // Get movie id to update
        Integer movieId = updateMovieRemoveGenreRequest.getMovieId();

        // Get genres to remove
        List<GenreDto> genreDtos = new ArrayList<>();
        updateMovieRemoveGenreRequest.getGenre().forEach(genre -> genreDtos.add(GenreSoapMapper.MAPPER.toGenreDto(genre)));

        // Update movie with genres to remove
        MovieDto movieResponseDto = movieService.updateMovieRemoveGenre(movieId, genreDtos);

        // Map movieResponseDto to movieResponse
        Movie movieResponse = MovieSoapMapper.MAPPER.toMovie(movieResponseDto);

        // Create updateMovieRemoveGenreResponse with movieResponse
        UpdateMovieRemoveGenreResponse updateMovieRemoveGenreResponse = new UpdateMovieRemoveGenreResponse();
        updateMovieRemoveGenreResponse.setMovie(movieResponse);

        // Return updateMovieRemoveGenreResponse
        return updateMovieRemoveGenreResponse;
    }

    public UpdateMovieAddCastResponse updateSoapMovieAddCast(UpdateMovieAddCastRequest updateMovieAddCastRequest) {
        // Get movie id to update
        Integer movieId = updateMovieAddCastRequest.getMovieId();

        // Get cast to add
        List<RoleDto> castDto = new ArrayList<>();
        updateMovieAddCastRequest.getCast().getRole().forEach(role -> castDto.add(RoleSoapMapper.MAPPER.toRoleDto(role)));

        // Update movie with genres to add
        MovieDto movieResponseDto = movieService.updateMovieAddCast(movieId, castDto);

        // Map movieResponseDto to movieResponse
        Movie movieResponse = MovieSoapMapper.MAPPER.toMovie(movieResponseDto);

        // Create updateMovieAddCastResponse with movieResponse
        UpdateMovieAddCastResponse updateMovieAddCastResponse = new UpdateMovieAddCastResponse();
        updateMovieAddCastResponse.setMovie(movieResponse);

        // Return updateMovieAddCastResponse
        return updateMovieAddCastResponse;
    }

    public UpdateMovieRemoveCastResponse updateSoapMovieRemoveCast(UpdateMovieRemoveCastRequest updateMovieRemoveCastRequest) {
        // Get movie id to update
        Integer movieId = updateMovieRemoveCastRequest.getMovieId();

        // Get genres to remove
        List<RoleDto> castDto = new ArrayList<>();
        updateMovieRemoveCastRequest.getCast().getRole().forEach(role -> castDto.add(RoleSoapMapper.MAPPER.toRoleDto(role)));

        // Update movie with genres to remove
        MovieDto movieResponseDto = movieService.updateMovieRemoveCast(movieId, castDto);

        // Map movieResponseDto to movieResponse
        Movie movieResponse = MovieSoapMapper.MAPPER.toMovie(movieResponseDto);

        // Create updateMovieRemoveCastResponse with movieResponse
        UpdateMovieRemoveCastResponse updateMovieRemoveCastResponse = new UpdateMovieRemoveCastResponse();
        updateMovieRemoveCastResponse.setMovie(movieResponse);

        // Return updateMovieRemoveCastResponse
        return updateMovieRemoveCastResponse;
    }

    public DeleteMovieResponse deleteSoapMovie(DeleteMovieRequest deleteMovieRequest) {
        movieService.delete(deleteMovieRequest.getId());

        DeleteMovieResponse deleteMovieResponse = new DeleteMovieResponse();
        Movie movie = new Movie();
        movie.setId(deleteMovieRequest.getId());
        deleteMovieResponse.setMovie(movie);
        return deleteMovieResponse;
    }
}
