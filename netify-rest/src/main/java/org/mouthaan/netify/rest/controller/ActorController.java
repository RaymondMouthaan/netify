package org.mouthaan.netify.rest.controller;

import io.swagger.annotations.*;

import org.mouthaan.netify.rest.utils.Util;
import org.mouthaan.netify.service.ActorService;
import org.mouthaan.netify.service.dto.ActorDto;
import org.mouthaan.netify.service.dto.CountDto;
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

@RestController
@Validated
@RequestMapping(value = "/actors")
@Api(value = "netify-actors", description = "Operations pertaining to actors in Netify" )
public class ActorController {

    private ActorService actorService;
    private final MessageSource messageSource;

    @Autowired
    public ActorController(ActorService actorService, MessageSource messageSource) {
        this.actorService = actorService;
        this.messageSource = messageSource;
    }

    /**
     * Count all actors.
     *
     * @return Returns total number of actors.
     */
    @ApiOperation(value = "Count actors", notes = "Count number of actors")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully counted actors"),
    })
    @CrossOrigin
    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity countAll() {
        CountDto countDto = this.actorService.countAll();
        return new ResponseEntity<>(countDto, HttpStatus.OK);
    }

    /**
     * Find all actors.
     *
     * @return Returns a list of all actors.
     */
    @ApiOperation(value = "View a list of all available actors", notes = "View a list of all available actors")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = ActorDto[].class),
    })
    @CrossOrigin
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity findAll(
            @ApiParam(value = "name", defaultValue = "Matt Damon")
            @RequestParam(value = "name", required = false) String name,
            @ApiParam(value = "gender", defaultValue = "MALE")
            @RequestParam(value = "gender", required = false) String gender) {

        /* ToDo: hashmap als input param */
        Map<String,String> filterParams = new HashMap();
        if (name != null) filterParams.put("name",name);
        if (gender != null) filterParams.put("gender",gender.toUpperCase());

        List<ActorDto> actors = actorService.findAll(filterParams);
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    /**
     * Find all actors by page.
     * Example request : http://localhost:8082/actors/paginated?page=2&amp;size=2&amp;sort=title,desc
     *
     * @param pageable A pageable object
     * @return Returns a list of all actors per page.
     */
    @ApiOperation(value = "Find all actors per page", notes = "Find all actors per page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = ActorDto[].class),
    })
    @CrossOrigin
    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity findPaginated(
            @PageableDefault(value = 25, page = 0, direction = Sort.Direction.ASC, sort = {"id"}) Pageable pageable) {
        Page<ActorDto> actors = actorService.findPaginated(pageable);
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    /**
     * Find an actor by id.
     *
     * @param id An actor id.
     * @return Returns a actor.
     */
    @ApiOperation(value = "Find an Actor with an id", notes = "Find an Actor with an id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ActorDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found"),
    })
    @CrossOrigin
    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity findById(
            @ApiParam(value = "An Actor's id", defaultValue = "996701", required = true)
            @PathVariable(value = "id") Integer id) {
        ActorDto actor = actorService.findById(id);
        return new ResponseEntity<>(actor, HttpStatus.OK);
    }

    /**
     * Adding an new actor.
     *
     * @param actor An actor to add.
     * @return Returns added actor.
     */
    @ApiOperation(value = "Add an actor", notes = "Adding a new actor")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = ActorDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 409, message = "Conflict"),
    })
    @CrossOrigin
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity add(
            @Valid @RequestBody ActorDto actor) {
        return new ResponseEntity<>(actorService.add(actor), HttpStatus.CREATED);
    }

    /**
     * Update an actor.
     *
     * @param id    An actor id.
     * @param actorDto An actor.
     * @return Returns updated actor.
     */
    @ApiOperation(value = "Update an actor", notes = "Updating an existing actor")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = ActorDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @CrossOrigin
    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(
            @ApiParam(value = "An actor id to update", required = true)
            @PathVariable("id") Integer id,
            @ApiParam(value = "An actor", required = true)
            @Valid @RequestBody ActorDto actorDto) {

        return new ResponseEntity<>(actorService.update(id, actorDto), HttpStatus.OK);
    }

    /**
     * Delete an actor.
     *
     * @param id An actor id.
     * @return Returns deleted actor.
     */
    @ApiOperation(value = "Delete an actor", notes = "Deleting an existing actor")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = ActorDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @CrossOrigin
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity delete(
            @ApiParam(value = "An actor id", required = true)
            @PathVariable("id") Integer id) {
        actorService.delete(id);
        return Util.handleSuccessAcceptance(messageSource, "element.type.actor", "acceptance.delete.success", Long.valueOf(id));
    }
}
