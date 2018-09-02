package org.mouthaan.netify.rest.controller;

import io.swagger.annotations.*;
import org.mouthaan.netify.rest.utils.Util;
import org.mouthaan.netify.service.BugService;
import org.mouthaan.netify.service.GenreService;
import org.mouthaan.netify.service.dto.CountDto;
import org.mouthaan.netify.service.dto.GenreDto;
import org.mouthaan.netify.rest.utils.Util;
import org.mouthaan.netify.service.BugService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@RequestMapping(value = "/genres")
@Api(value = "netflux-genres", description = "Operations pertaining to genres in Netflux")
public class GenreController {
    private GenreService genreService;
    private final BugService bugService;
    private final MessageSource messageSource;

    @Autowired
    public GenreController(GenreService genreService, BugService bugService, MessageSource messageSource) {
        this.genreService = genreService;
        this.bugService = bugService;
        this.messageSource = messageSource;
    }

    /**
     * Count all genres.
     *
     * @return Returns total number of genres.
     */
    @ApiOperation(value = "Count genres", notes = "Count number of genres")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully counted genres"),
    })
    @CrossOrigin
    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity countAll() {
        CountDto countDto = this.genreService.countAll();
        return new ResponseEntity<>(countDto, HttpStatus.OK);
    }

    /**
     * Find all genres.
     *
     * @return Returns a list of all genres.
     */
    @ApiOperation(value = "View a list of all available genres", notes = "View a list of all available genres")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = GenreDto[].class),
    })
    @CrossOrigin
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity findAll(
            @ApiParam(value = "name", defaultValue = "Horror")
            @RequestParam(value = "name", required = false) String name) {

        /* ToDo: hashmap als input param */
        Map<String,String> filterParams = new HashMap();
        if (name != null) filterParams.put("name",name);
        List<GenreDto> genres = genreService.findAll(filterParams);
        GenreDto genreDto = new GenreDto();
        if (bugService.findByBugKey("bug_210").getBugEnabled()) {
            genreDto.setId(112);
            genreDto.setName("TestGenre");
            genres.add(genreDto);
        }
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

    // Example request : http://localhost:8082/genres/paginated?page=2&size=2&sort=title,desc

    /**
     * Find all genres by page.
     * Example request : http://localhost:8082/genres/paginated?page=2&amp;size=2&amp;sort=name,desc
     *
     * @param pageable A pageable object
     * @return Returns a list of all genres by page.
     */
    @ApiOperation(value = "Find all genres per page", notes = "Find all actors per page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = GenreDto[].class),
    })
    @CrossOrigin
    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity findPaginated(
            @PageableDefault(value = 25, page = 0, direction = Sort.Direction.ASC, sort = {"id"}) Pageable pageable) {
        Page<GenreDto> genres = genreService.findPaginated(pageable);
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

    /**
     * Find a genre by id.
     *
     * @param id A genre id.
     * @return Returns a genre.
     */
    @ApiOperation(value = "Find a genre by id", notes = "Find a genre by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = GenreDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found"),
    })
    @CrossOrigin
    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity findById(
            @ApiParam(value = "A genre id", defaultValue = "53", required = true)
            @PathVariable(value = "id") Integer id) {
        GenreDto genre = genreService.findById(id);
        return new ResponseEntity<>(genre, HttpStatus.OK);
    }

    /**
     * Adding a new genre.
     *
     * @param genre A genre to add.
     * @return Returns added genre.
     */
    @ApiOperation(value = "Add a genre", notes = "Adding a new genre")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = GenreDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 409, message = "Conflict"),
    })
    @CrossOrigin
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity add(
            @Valid @RequestBody GenreDto genre) {
        return new ResponseEntity<>(genreService.add(genre), HttpStatus.CREATED);
    }

    /**
     * Update a genre.
     *
     * @param id    A genre name.
     * @param genre A genre.
     * @return Returns updated genre.
     */
    @ApiOperation(value = "Update a genre", notes = "Updating an existing genre")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = GenreDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @CrossOrigin
    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity update(
            @ApiParam(value = "A genre id", required = true)
            @PathVariable("id") Integer id,
            @ApiParam(value = "A genre", required = true)
            @Valid @RequestBody GenreDto genre) {
        return new ResponseEntity<>(genreService.update(id, genre), HttpStatus.OK);
    }

    /**
     * Delete a genre.
     *
     * @param id A genre id.
     * @return Returns custom message of deleted genre.
     */
    @ApiOperation(value = "Delete a genre", notes = "Deleting an existing genre")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = GenreDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @CrossOrigin
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity delete(
            @ApiParam(value = "A genre id", required = true)
            @PathVariable("id") Integer id) {
        genreService.delete(id);
        return Util.handleSuccessAcceptance(messageSource, "element.type.genre", "acceptance.delete.success", Long.valueOf(id));
    }


}
