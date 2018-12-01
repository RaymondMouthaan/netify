package org.mouthaan.netify.rest.controller;


import io.swagger.annotations.*;
import org.mouthaan.netify.rest.utils.Util;
import org.mouthaan.netify.service.BugService;
import org.mouthaan.netify.service.MovieService;
import org.mouthaan.netify.service.dto.CountDto;
import org.mouthaan.netify.service.dto.GenreDto;
import org.mouthaan.netify.service.dto.MovieDto;
import org.mouthaan.netify.service.dto.RoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.*;

/**
 * Created by in482mou on 23-6-2017.
 * <p>
 * - Mappings are lowercase
 * - To Create a resource : HTTP POST should be used
 * - To Retrieve a resource : HTTP GET should be used
 * - To Update a resource : HTTP PUT should be used
 * - To Delete a resource : HTTP DELETE should be used
 */
@RestController
@Validated
@RequestMapping(value = "/movies")
@Api(value = "netify-movies", description = "Operations pertaining to movies in Netify")
public class MovieController {
    private MovieService movieService;

    private final MessageSource messageSource;
    private BugService bugService;

    @Autowired
    public MovieController(MovieService movieService, MessageSource messageSource, BugService bugService) throws Exception {
        this.movieService = movieService;
        this.messageSource = messageSource;
        this.bugService = bugService;
    }

    /**
     * Count all movies.
     *
     * @return Returns total number of movies.
     */
    @ApiOperation(value = "Count movies", notes = "Count number of movies")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully counted movies"),
    })
    @CrossOrigin
    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity countAll() {
        CountDto countDto = this.movieService.countAll();
        return new ResponseEntity<>(countDto, HttpStatus.OK);
    }

    /**
     * Find all movies.
     *
     * @return Returns a list of all movies.
     */
    @ApiOperation(value = "View a list of all available movies", notes = "View a list of all available movies")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = MovieDto[].class),
    })
    @CrossOrigin
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity findAll(
            @ApiParam(value = "title", defaultValue = "The Godfather")
            @RequestParam(value = "title", required = false) String title,
            @ApiParam(value = "min_year", defaultValue = "1972")
            @RequestParam(value = "min_year", required = false) Integer minYear,
            @ApiParam(value = "max_year", defaultValue = "1973")
            @RequestParam(value = "max_year", required = false) Integer maxYear,
            @ApiParam(value = "min_runtime", defaultValue = "135")
            @RequestParam(value = "min_runtime", required = false) Integer minRuntime,
            @ApiParam(value = "genreId", defaultValue = "80")
            @RequestParam(value = "genreId", required = false) String genreId,
            @ApiParam(value = "max_runtime", defaultValue = "175")
            @RequestParam(value = "max_runtime", required = false) Integer maxRuntime,
            @ApiParam(value = "min_popularity", defaultValue = "6.51")
            @RequestParam(value = "min_popularity", required = false) Double minPopularity,
            @ApiParam(value = "max_popularity", defaultValue = "6.89")
            @RequestParam(value = "max_popularity", required = false) Double maxPopularity)

            throws ParseException {
        Map<String, String> filterParams = new HashMap<>();

        if (title != null)          filterParams.put("title", title);
        if (minYear != null)        filterParams.put("min_year", minYear+"");
        if (maxYear != null)        filterParams.put("max_year", maxYear+"");
        if (minRuntime != null)     filterParams.put("min_runtime", minRuntime+"");
        if (maxRuntime != null)     filterParams.put("max_runtime", maxRuntime+"");
        if (minPopularity != null)  filterParams.put("min_popularity", minPopularity+"");
        if (maxPopularity != null)  filterParams.put("max_popularity", maxPopularity+"");

        if (genreId != null) filterParams.put("genreId", genreId);

        List<MovieDto> movies = movieService.findAll(filterParams);
        List<MovieDto> tmpMovies = new ArrayList<>();

        if (bugService.findByBugKey("bug_212").getBugEnabled()) {
            for (MovieDto movie : movies) {
               Set<RoleDto> unsortedCast = new TreeSet<>(movie.getCast());
               movie.setCast(unsortedCast);
            }

        }

        // Voor de advanced experience int num =  ThreadLocalRandom.current().nextInt(1,10);
        if (bugService.findByBugKey("bug_211").getBugEnabled() && movies.size() > 4 && filterParams.size() != 0) {
            for (int i = 0; i < 5; i++) {
                tmpMovies.add(movies.get(i));
            }
            return new ResponseEntity<>(tmpMovies, HttpStatus.OK);
        }




        if (bugService.findByBugKey("bug_206").getBugEnabled()) {
            return new ResponseEntity<>(movies, HttpStatus.ACCEPTED);
        }


        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    /**
     * Find all movies by page.
     * Example request : http://localhost:8082/movies/paginated?page=2&amp;size=2&amp;sort=title,desc
     *
     * @param pageable A pageable object
     * @return Returns a list of all movies by page.
     */
    @ApiOperation(value = "Find all movies per page", notes = "Find all movies per page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = MovieDto[].class),
    })
    @CrossOrigin
    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity findPaginated(
            @PageableDefault(value = 25, page = 0, direction = Sort.Direction.ASC, sort = {"id"}) Pageable pageable) {
        Page<MovieDto> movies = movieService.findPaginated(pageable);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    /**
     * Find a movie by id.
     *
     * @param id A movie id.
     * @return Returns a movie.
     */
    @ApiOperation(value = "Find a movie by id", notes = "Find a movie by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = MovieDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found"),
    })
    @CrossOrigin
    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity findById(
            @ApiParam(value = "A movie id", defaultValue = "2503", required = true)
            @PathVariable(name = "id") Integer id) {
        MovieDto movie = movieService.findById(id);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    /**
     * Adding a new movie.
     *
     * @param movie A movie to add.
     * @return Returns added movie.
     */
    @ApiOperation(value = "Add a movie", notes = "Adding a new movie")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = MovieDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 409, message = "Conflict"),
    })
    @CrossOrigin
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity add(
            @Valid @RequestBody MovieDto movie) {
        if ( bugService.findByBugKey("bug_204").getBugEnabled()) {
            movie.setHomepage("");
            movie.setOriginalTitle("");
            movie.setTagline("");
        }
        if (bugService.findByBugKey("bug_207").getBugEnabled()) {
            movie.setRuntime(movie.getRuntime() * 60);
        }
        if (bugService.findByBugKey("bug_208").getBugEnabled()) {
            Set<GenreDto> tmpGenres = new HashSet<>();
            //Onderstaande is zo bedoeld! Afblijven
            for (GenreDto genre : movie.getGenres()) {
                tmpGenres.add(genre);
                movie.setGenres(tmpGenres);
                break;
            }
        }
        if (bugService.findByBugKey("bug_203").getBugEnabled()) {
            //Onderstaande is zo bedoeld! Afblijven
            MovieDto noShowMovie = movieService.add(movie);
            noShowMovie = null;
            return new ResponseEntity<>(noShowMovie, HttpStatus.CREATED);
        }

        if (bugService.findByBugKey("bug_209").getBugEnabled()) {
            return new ResponseEntity<>(movieService.add(movie), HttpStatus.I_AM_A_TEAPOT);
        }

        return new ResponseEntity<>(movieService.add(movie), HttpStatus.CREATED);
    }


    /**
     * Update a movie.
     *
     * @param id    A movie id.
     * @param movie A movie.
     * @return Returns updated movie.
     */
    @ApiOperation(value = "Update a movie", notes = "Updating an existing movie")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = MovieDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @CrossOrigin
    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity update(
            @ApiParam(value = "A movie id", required = true)
            @PathVariable("id") Integer id,
            @ApiParam(value = "A movie", required = true)
            @Valid @RequestBody MovieDto movie) {
        return new ResponseEntity<>(movieService.update(id, movie), HttpStatus.OK);
    }

    /**
     * Update a movie with genres to add.
     *
     * @param id     A movie id.
     * @param genres A list of genres to add.
     * @return Returns updated movie.
     */
    @ApiOperation(value = "Update a movie", notes = "Updating an existing movie")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = MovieDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @CrossOrigin
    @PutMapping(value = "/update/{id}/add/genre", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity updateMovieAddGenre(
            @ApiParam(value = "A movie id", required = true)
            @PathVariable("id") Integer id,
            @ApiParam(value = "A movie", required = true)
            @Valid @RequestBody List<GenreDto> genres) {
        return new ResponseEntity<>(movieService.updateMovieAddGenre(id, genres), HttpStatus.OK);
    }

    /**
     * Update a movie with genres to remove.
     *
     * @param id     A movie id.
     * @param genres A list of genres to remove.
     * @return Returns updated movie.
     */
    @ApiOperation(value = "Update a movie", notes = "Updating an existing movie")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = MovieDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @CrossOrigin
    @PutMapping(value = "/update/{id}/remove/genre", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity updateMovieRemoveGenre(
            @ApiParam(value = "A movie id", required = true)
            @PathVariable("id") Integer id,
            @ApiParam(value = "A cast", required = true)
            @Valid @RequestBody List<GenreDto> genres) {
        return new ResponseEntity<>(movieService.updateMovieRemoveGenre(id, genres), HttpStatus.OK);
    }

    /**
     * Update a movie with roles to add.
     *
     * @param id   A movie id.
     * @param cast A list of roles to add.
     * @return Returns updated movie.
     */
    @ApiOperation(value = "Update a movie", notes = "Updating an existing movie")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = MovieDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @CrossOrigin
    @PutMapping(value = "/update/{id}/add/cast", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity updateMovieAddCast(
            @ApiParam(value = "A movie id", required = true)
            @PathVariable("id") Integer id,
            @ApiParam(value = "A movie", required = true)
            @Valid @RequestBody List<RoleDto> cast) {
        return new ResponseEntity<>(movieService.updateMovieAddCast(id, cast), HttpStatus.OK);
    }

    /**
     * Update a movie with roles to remove.
     *
     * @param id   A movie id.
     * @param cast A list of roles to add.
     * @return Returns updated movie.
     */
    @ApiOperation(value = "Update a movie", notes = "Updating an existing movie")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = MovieDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @CrossOrigin
    @PutMapping(value = "/update/{id}/remove/cast", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity updateMovieRemoveCast(
            @ApiParam(value = "A movie id", required = true)
            @PathVariable("id") Integer id,
            @ApiParam(value = "A cast", required = true)
            @Valid @RequestBody List<RoleDto> cast) {
        return new ResponseEntity<>(movieService.updateMovieRemoveCast(id, cast), HttpStatus.OK);
    }

    /**
     * Delete a movie.
     *
     * @param id A movie id.
     * @return Returns custom message of deleted movie.
     */
    @ApiOperation(value = "Delete a movie", notes = "Deleting an existing movie")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = GenreDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @CrossOrigin
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity delete(
            @ApiParam(value = "A movie id", required = true)
            @PathVariable("id") Integer id) {
        movieService.delete(id);
        return Util.handleSuccessAcceptance(messageSource, "element.type.movie", "acceptance.delete.success", Long.valueOf(id));
    }

}
