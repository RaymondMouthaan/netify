package org.mouthaan.netify.rest.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.mouthaan.netify.service.ActorService;
import org.mouthaan.netify.service.dto.CountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    // @PerformanceLoggingEntryPoint Todo: Disabled until logX
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
}
