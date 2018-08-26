package org.mouthaan.netify.rest.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping(value = "/actors")
@Api(value = "netflux-actors", description = "Operations pertaining to actors in Netflux")
public class TestController {

    @ApiOperation(value = "Test Controller", notes = "Bla")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully counted actors"),
    })
    @GetMapping("/count")
    public String count() {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        Map<String, String> env = System.getenv();
        Properties prop = System.getProperties();
        String hostName = "";

        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return "Hello Docker World! Today is " + localDate + " and the time is " + localTime + " Hostname " + hostName;
    }
}
