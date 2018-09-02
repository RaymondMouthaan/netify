package org.mouthaan.netify.rest.controller;

import io.swagger.annotations.*;
import org.mouthaan.netify.service.RoleService;
import org.mouthaan.netify.service.dto.CountDto;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by in482mou on 25-7-2017.
 * <p>
 * - Mappings are lowercase
 * - To Create a resource : HTTP POST should be used
 * - To Retrieve a resource : HTTP GET should be used
 * - To Update a resource : HTTP PUT should be used
 * - To Delete a resource : HTTP DELETE should be used
 */
@RestController
@Validated
@RequestMapping(value = "/roles")
@Api(value = "netflux-roles", description = "Operations pertaining to roles of actors in movies in Netflux")
public class RoleController {
    private final RoleService roleService;
    private final MessageSource messageSource;

    @Autowired
    public RoleController(RoleService roleService, MessageSource messageSource) {
        this.roleService = roleService;
        this.messageSource = messageSource;
    }

    /**
     * Count all Roles.
     *
     * @return Returns total number of roles.
     */
    @ApiOperation(value = "Count roles", notes = "Count number of roles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully counted roles"),
    })
    @CrossOrigin
    @GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity countAll() {
        CountDto countDto = this.roleService.countAll();
        return new ResponseEntity<>(countDto, HttpStatus.OK);
    }

    /**
     * Find all roles.
     *
     * @return Returns a list of all roles.
     */
    @ApiOperation(value = "View a list of all available roles", notes = "View a list of all available roles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = RoleDto[].class),
    })
    @CrossOrigin
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity findAll(
            @ApiParam(value = "character", defaultValue = "Jason Bourne")
            @RequestParam(value = "character", required = false) String character,
            @ApiParam(value = "order", defaultValue = "0")
            @RequestParam(value = "order", required = false) String order,
            @ApiParam(value = "movieId", defaultValue = "2503")
            @RequestParam(value = "movieId", required = false) Integer movieId,
            @ApiParam(value = "actorId", defaultValue = "1892")
            @RequestParam(value = "actorId", required = false) Integer actorId) {

        /* ToDo: hashmap als input param */
        Map<String, String> filterParams = new HashMap<>();
        if (character != null) filterParams.put("character", character);
        if (order != null) filterParams.put("order", order);
        if (movieId != null) filterParams.put("movieId", movieId + "");

        // Todo : bug search with actorId gives error, because Role.class does not have actorId field
        if (actorId != null) filterParams.put("actorId", actorId + "");

        List<RoleDto> roles = roleService.findAll(filterParams);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    /**
     * Find all roles by page.
     * Example request : http://localhost:8082/roles/paginated?page=2&amp;size=2&amp;sort=title,desc
     *
     * @param pageable A pageable object
     * @return Returns a list of all roles per page.
     */
    @ApiOperation(value = "Find all roles per page", notes = "Find all roles per page")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = RoleDto[].class),
    })
    @CrossOrigin
    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity findPaginated(
            @PageableDefault(value = 25, page = 0, direction = Sort.Direction.ASC, sort = {"id"}) Pageable pageable) {
        Page<RoleDto> roles = roleService.findPaginated(pageable);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    /**
     * Find an role by id.
     *
     * @param id An role id.
     * @return Returns a role.
     */
    @ApiOperation(value = "Find a Role with an id", notes = "Find a Role with an id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = RoleDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found"),
    })
    @CrossOrigin
    @GetMapping(value = "/find/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity findById(
            @ApiParam(value = "A Role's id", defaultValue = "2500", required = true)
            @PathVariable(value = "id") Integer id) {
        RoleDto role = roleService.findById(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}
