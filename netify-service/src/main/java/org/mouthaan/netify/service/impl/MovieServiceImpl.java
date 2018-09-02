package org.mouthaan.netify.service.impl;

import org.mouthaan.netify.domain.dao.BaseDao;
import org.mouthaan.netify.domain.dao.SearchCriteria;
import org.mouthaan.netify.domain.model.Movie;
import org.mouthaan.netify.domain.repository.MovieRepository;
import org.mouthaan.netify.service.ActorService;
import org.mouthaan.netify.service.GenreService;
import org.mouthaan.netify.service.MovieService;
import org.mouthaan.netify.service.RoleService;
import org.mouthaan.netify.service.dto.*;
import org.mouthaan.netify.service.exception.AlreadyExistException;
import org.mouthaan.netify.service.exception.NotFoundException;
import org.mouthaan.netify.service.mapper.MovieServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("movieService")
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final GenreService genreService;
    private final ActorService actorService;
    private final RoleService roleService;
    private final MovieServiceMapper movieServiceMapper;
    private final BaseDao baseDAO;
    private final BugServiceImpl bugService;



    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, GenreService genreService, ActorService actorService, RoleService roleService, MovieServiceMapper movieServiceMapper, BaseDao baseDAO, BugServiceImpl bugService) {
        this.movieRepository = movieRepository;
        this.genreService = genreService;
        this.actorService = actorService;
        this.roleService = roleService;
        this.movieServiceMapper = movieServiceMapper;
        this.baseDAO = baseDAO;
        this.bugService = bugService;
    }

    @Override
    public CountDto countAll() {
        return new CountDto(this.movieRepository.count());
    }

    public List<MovieDto> findAll() {
        return this.movieRepository.findAll().stream()
                .map(entity -> movieServiceMapper.map(entity, MovieDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDto> findAll(Map<String, String> filterParams) {
        List<String> genreIds = new ArrayList<>();

        List<SearchCriteria> queryParams = new ArrayList<>();
        filterParams.forEach((k,v)->{
            if (k.equalsIgnoreCase("title")) queryParams.add(new SearchCriteria(k, bugService.findByBugKey("bug_201").getBugEnabled() ? "=" : "%", v));
            if (k.equalsIgnoreCase("min_year"))queryParams.add(new SearchCriteria("releaseDate", ">", v + (bugService.findByBugKey("bug_202").getBugEnabled() ? "" : "-01-01")));
            if (k.equalsIgnoreCase("max_year")) queryParams.add(new SearchCriteria("releaseDate", "<", v + (bugService.findByBugKey("bug_202").getBugEnabled() ? "" : "-12-31")));
            if (k.equalsIgnoreCase("min_runtime"))queryParams.add(new SearchCriteria("runtime", ">", v));
            if (k.equalsIgnoreCase("max_runtime"))queryParams.add(new SearchCriteria("runtime", "<", v));
            if (k.equalsIgnoreCase("min_popularity"))queryParams.add(new SearchCriteria("popularity", ">", v));
            if (k.equalsIgnoreCase("max_popularity"))queryParams.add(new SearchCriteria("popularity", "<", v));
            if (k.equalsIgnoreCase("genreid")) {
                String[] genres = v.split(",");
                genreIds.addAll(Arrays.asList(genres));
            }
        });

        List<MovieDto> movies = this.baseDAO.findAllPredicated(queryParams, Movie.class).stream()
                .map(entity -> movieServiceMapper.map(entity, MovieDto.class))
                .collect(Collectors.toList());
        if (movies.size() == 0) {
            String filter = queryParams.stream().map(SearchCriteria::toString)
                    .collect(Collectors.joining("; "));
            throw new NotFoundException("element.type.movie", filter);
        }

        //todo genre via queryBuilder? Want dit is foeilelijk!
        if (genreIds.size() > 0) {
            List<MovieDto> filteredMovies = new ArrayList<>();
            List<GenreDto> genreDtos = new ArrayList<>();
            for (String genreIdLoc : genreIds) {
                genreDtos.add(genreService.findById(Integer.parseInt(genreIdLoc)));
            }

            for (MovieDto movie : movies) {
                boolean addMovie = true;
                for (GenreDto genreDto : genreDtos) {
                    if (!movie.getGenres().contains(genreDto)) {
                        addMovie = false;
                        break;
                    }
                }
                if (addMovie) filteredMovies.add(movie);
            }

            if (filteredMovies.size() == 0) {
                String filter = queryParams.stream().map(SearchCriteria::toString)
                        .collect(Collectors.joining("; "));
                throw new NotFoundException("element.type.movie", filter);
            }
            return filteredMovies;
        }
        return movies;
    }


    @Override
    public Page<MovieDto> findPaginated(Pageable pageable) {
        return this.movieRepository.findAll(pageable)
                .map(entity -> movieServiceMapper.map(entity, MovieDto.class));
    }

    @Override
    public MovieDto findById(Integer id) {
        Movie movie = this.movieRepository.getOne(id);
        if (null == movie) {
            throw new NotFoundException("element.type.movie", "id=\'" + String.valueOf(id) + "\'");
        }
        return movieServiceMapper.map(movie, MovieDto.class);
    }

    @Override
    public MovieDto add(MovieDto movieDto) {
        if (this.isExists(movieDto)) {
            throw new AlreadyExistException("element.type.movie", movieDto.getTitle() + " from " + movieDto.getReleaseDate());
        }
        movieDto.setId(-1); // temp movieId (workaround)
        processGenres(movieDto);
        movieDto = processRoles(movieDto);

        return movieServiceMapper.map(this.movieRepository.saveAndFlush(movieServiceMapper.map(movieDto, Movie.class)), MovieDto.class);
    }

    @Override
    public MovieDto update(Integer id, MovieDto movieDto) {
        Optional<Movie> movie = this.movieRepository.findById(id);
        if (movie.isPresent()) {
            // Ignore genres for update: Set genres to null
            if (movieDto.getGenres() != null) { movieDto.setGenres(null); }

            // Ignore cast for update: Set cast to null
            if (movieDto.getCast() != null) { movieDto.setCast(null); }

            // Map movieDto fields to updated movie (nulls are ignored)
            movieServiceMapper.map(movieDto, movie);
            Movie updateMovie = movieRepository.saveAndFlush(movie.get());
            return movieServiceMapper.map(updateMovie, MovieDto.class);
        } else {
            throw new NotFoundException("element.type.movie", "id=\'" + String.valueOf(id) + "\'");
        }
    }

    /**
     * Add genres to movie
     *
     * @param id        A movie id.
     * @param genresDto List of genres to add.
     * @return Returns updated movie.
     */
    @Override
    public MovieDto updateMovieAddGenre(Integer id, List<GenreDto> genresDto) {
        Optional<Movie> movie = this.movieRepository.findById(id);
        if (!movie.isPresent()) {
            // Map found movie to movieDto
            MovieDto movieDto = movieServiceMapper.map(movie, MovieDto.class);

            // For each genre check if it exists and add it to movieDto
            genresDto.forEach(genreDto -> {
                if (genreService.isExists(genreDto)) {
                    movieDto.getGenres().add(genreService.findByName(genreDto.getName()));
                } else {
                    movieDto.getGenres().add(genreService.add(genreDto));
                }
            });

            // Map movieDto to movie
            Movie updatedMovie = movieServiceMapper.map(movieDto, Movie.class);

            // Save and flush updated movie
            Movie returnedMovie = movieRepository.saveAndFlush(updatedMovie);

            // Return returnedMovie
            return movieServiceMapper.map(returnedMovie, MovieDto.class);
        }
        else {
            throw new NotFoundException("element.type.movie", "id=\'" + String.valueOf(id) + "\'");
        }
    }

    /**
     * Remove genres from movie
     *
     * @param id        A movie id.
     * @param genresDto List of genres to remove.
     * @return Returns updated movie.
     */
    @Override
    public MovieDto updateMovieRemoveGenre(Integer id, List<GenreDto> genresDto) {
        Optional<Movie> movie = this.movieRepository.findById(id);
        if (!movie.isPresent()) {
            // Map found movie to movieDto
            MovieDto movieDto = movieServiceMapper.map(movie, MovieDto.class);

            // For each genre check if it exists and add it to a new list
            List<GenreDto> genresDtoToRemove = new ArrayList<>();
            genresDto.forEach(genreDto -> {
                if (genreService.isExists(genreDto)) {
                    genresDtoToRemove.add(genreService.findByName(genreDto.getName()));
                }
            });

            // For each genre check if it exists in movieDto and remove it
            genresDtoToRemove.forEach(genreDto -> {
                movieDto.getGenres().remove(genreDto);
            });

            // Map movieDto to movie
            Movie updatedMovie = movieServiceMapper.map(movieDto, Movie.class);

            // Save and flush updated movie
            Movie returnedMovie = movieRepository.saveAndFlush(updatedMovie);

            // Return returnedMovie
            return movieServiceMapper.map(returnedMovie, MovieDto.class);
        } else {
            throw new NotFoundException("element.type.movie", "id=\'" + String.valueOf(id) + "\'");
        }
    }

    /**
     * Add cast to movie
     *
     * @param id   A movie id.
     * @param cast List of roles to add.
     * @return Returns updated movie.
     */
    @Override
    public MovieDto updateMovieAddCast(Integer id, List<RoleDto> cast) {
        Optional<Movie> movie = this.movieRepository.findById(id);
        if (movie.isPresent()) {
            // Map found movie to movieDto
            MovieDto movieDto = movieServiceMapper.map(movie, MovieDto.class);

            processRoles(movieDto, new LinkedHashSet<>(cast));

            // Map movieDto to movie
            Movie updatedMovie = movieServiceMapper.map(movieDto, Movie.class);

            // Save and flush updated movie
            Movie returnedMovie = movieRepository.saveAndFlush(updatedMovie);

            // Return returnedMovie
            return movieServiceMapper.map(returnedMovie, MovieDto.class);
        } else {
            throw new NotFoundException("element.type.movie", "id=\'" + String.valueOf(id) + "\'");
        }

    }

    /**
     * Remove cast from movie
     *
     * @param id   A movie id.
     * @param cast List of roles to remove.
     * @return Returns updated movie.
     */
    @Override
    public MovieDto updateMovieRemoveCast(Integer id, List<RoleDto> cast) {
        Optional<Movie> movie = this.movieRepository.findById(id);
        if (movie.isPresent()) {
            // Map found movie to movieDto
            MovieDto movieDto = movieServiceMapper.map(movie, MovieDto.class);

            Set<RoleDto> currentCast = new HashSet<>(movieDto.getCast());
            cast.forEach(castDto ->
                    movieDto.getCast().forEach(currentRole -> {
                        if (currentRole.equals(castDto)) {
                            currentCast.remove(currentRole);
                        }
                    }));

            movieDto.setCast(currentCast);


            // Map movieDto to movie
            Movie updatedMovie = movieServiceMapper.map(movieDto, Movie.class);


            // Save and flush updated movie
            Movie returnedMovie = movieRepository.saveAndFlush(updatedMovie);

            // Return returnedMovie
            return movieServiceMapper.map(returnedMovie, MovieDto.class);
        } else {
            throw new NotFoundException("element.type.movie", "id=\'" + String.valueOf(id) + "\'");
        }
    }

    @Override
    public void delete(Integer id) {
        Optional<Movie> movie = this.movieRepository.findById(id);
        if (movie.isPresent()) {
            this.movieRepository.deleteById(id);
//            this.movieRepository.deleteById(movie.get);
        }
        else {
            throw new NotFoundException("element.type.movie", "id=\'" + String.valueOf(id) + "\'");
        }

    }

    @Override
    public boolean isExists(MovieDto movieDto) {
        Movie movie = movieServiceMapper.map(movieDto, Movie.class);
        return movieRepository.findByTitleAndReleaseDate(movie.getTitle(), movie.getReleaseDate()) != null;
    }

    private void processGenres(MovieDto movieDto) {
        if (null == movieDto.getGenres() || movieDto.getGenres().size() <= 0) {
            return;
        }
        Set<GenreDto> newGenres = new HashSet<>(movieDto.getGenres());
        movieDto.getGenres().clear();

        newGenres.forEach(genre -> {
            if (genreService.isExists(genre)) {
                movieDto.getGenres().add(genreService.findByName(genre.getName()));
            } else {
                movieDto.getGenres().add(genreService.add(genre));
            }
        });
    }

    // Process Roles for Add Movie
    private MovieDto processRoles(MovieDto movieDto) {
        if (null == movieDto.getCast() || movieDto.getCast().size() <= 0) {
            return movieDto;
        }
        Set<RoleDto> newRoles = new HashSet<>(movieDto.getCast());
        movieDto.getCast().clear();
        return processRoles(movieDto, newRoles);
    }

    private MovieDto processRoles(MovieDto movieDto, Set<RoleDto> newRoles) {
        if (movieDto.getId() != null) {
            if (!movieRepository.existsById(movieDto.getId())) {
                movieDto = movieServiceMapper.map(this.movieRepository.saveAndFlush(movieServiceMapper.map(movieDto, Movie.class)), MovieDto.class);
            }
        }
        for (RoleDto roleDto : newRoles) {
            ActorDto actor;
            if (actorService.isExists(roleDto.getActor())) {
                actor = actorService.findByName(roleDto.getActor().getName());
            } else {
                actor = actorService.add(roleDto.getActor());
            }
            roleDto.setActor(actor);
            roleDto.setMovieId(movieDto.getId());
            RoleDto newRole = roleService.add(roleDto);
            movieDto.getCast().add(newRole);
        }
        return movieDto;
    }
}
