package org.mouthaan.netify.rest.controller;

import io.swagger.annotations.*;
import org.apache.commons.logging.Log;
import org.apache.tomcat.util.codec.binary.Base64;
import org.mouthaan.netify.domain.dao.SearchCriteria;
import org.mouthaan.netify.service.BugService;
import org.mouthaan.netify.service.dto.BugDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
@RequestMapping(value = "/bugs")
@Api(value = "netify-bugs", description = "Operations pertaining to bugs in Netify")
public class BugController {

    private static final String SECRET_KEY_1 = "ssdkF$HUy2A#D%kd";
    private static final String SECRET_KEY_2 = "weJiSEvR5yAC5ftB";

    private static IvParameterSpec ivParameterSpec;
    private static SecretKeySpec secretKeySpec;
    private static Cipher cipher;

    private BugService bugService;
    private Log log;

    @Autowired
    public BugController(BugService bugService) {
        this.bugService = bugService;
    }

    /**
     * Find a bug by key.
     */
    @ApiOperation(value = "Find a bug with a bug key", notes = "Find a bug with a key")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = BugDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found"),
    })
    @CrossOrigin
    @GetMapping(value = "/find/{bugKey}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity findByBugKey(
            @ApiParam(value = "A bug key", defaultValue = "bug_1", required = true)
            @PathVariable(value = "bugKey") String bugKey) {
        BugDto bugDto = bugService.findByBugKey(bugKey);
        decryptBug(bugDto, bugKey);
        return new ResponseEntity<>(bugDto, HttpStatus.OK);
    }



    /**
     * Find all bugs.
     */
    @ApiOperation(value = "Find all bugs", notes = "Find all bugs")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = BugDto[].class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found"),
    })
    @CrossOrigin
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity findAll(
            @ApiParam(value = "bug key", defaultValue = "bug_1")
            @RequestParam(value = "bugKey", required = false) String bugKey,
            @ApiParam(value = "bug enabled", defaultValue = "false")
            @RequestParam(value = "bugEnabled", required = false) Boolean bugEnabled,
            @ApiParam(value = "bug group", defaultValue = "1")
            @RequestParam(value = "bugGroup", required = false) Integer bugGroup,
            @ApiParam(value = "show minimum", defaultValue = "false")
            @RequestParam(value = "showMinimum", required = true) Boolean showMinimum) {

        List<SearchCriteria> queryParams = new ArrayList<>();
        if (bugKey != null) queryParams.add(new SearchCriteria("bugKey", "=", bugKey));
        if (bugEnabled != null) queryParams.add(new SearchCriteria("bugEnabled", "=", bugEnabled));
        if (bugGroup != null) queryParams.add(new SearchCriteria("bugGroup", "=", bugGroup));

        List<BugDto> bugDtos = bugService.findAll(queryParams);

        bugDtos.forEach(bugDto -> {
            if (showMinimum) {
                bugDto.setBugDescription(null);
                bugDto.setBugGroup(null);
                bugDto.setBugLocation(null);
            } else {
                decryptBug(bugDto, bugKey);
            }
        });
        return new ResponseEntity<>(bugDtos, HttpStatus.OK);
    }

    /**
     * Update a bug by key.
     */
    @ApiOperation(value = "Update a bug with a bug key", notes = "Update a bug with a key")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = BugDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found"),
    })
    @CrossOrigin
    @PutMapping(value = "/update/{bugKey}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity update(
            @ApiParam(value = "A bug key", defaultValue = "bug_1", required = true)
            @PathVariable(value = "bugKey") String bugKey,
            @ApiParam(value = "bug enabled", defaultValue = "false")
            @RequestParam(value = "bug enabled", required = false) Boolean bugEnabled) {
        BugDto bugDto = new BugDto(bugKey, null, bugEnabled, null, null);
        return new ResponseEntity<>(bugService.update(bugDto), HttpStatus.OK);
    }

    /**
     * Update all bugs based upon filter criteria
     */
    @ApiOperation(value = "Update all bugs based upon filter criteria", notes = "Update all bugs based upon filter criteria")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = BugDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Not found"),
    })
    @CrossOrigin
    @PutMapping(value = "/update/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity update(
            @ApiParam(value = "bug key filter")
            @RequestParam(value = "bugKey", required = false) String bugKey,
            @ApiParam(value = "bug enabled filter")
            @RequestParam(value = "bugEnabled", required = false) Boolean bugEnabled,
            @ApiParam(value = "bug group filter")
            @RequestParam(value = "bugGroup", required = false) Integer bugGroup,
            @ApiParam(value = "Update bug enabled", defaultValue = "false")
            @RequestParam(value = "updateBugEnabled") Boolean updateBugEnabled) {

        List<SearchCriteria> queryParams = new ArrayList<>();
        if (bugKey != null) queryParams.add(new SearchCriteria("bugKey", "=", bugKey));
        if (bugEnabled != null) queryParams.add(new SearchCriteria("bugEnabled", "=", bugEnabled));
        if (bugGroup != null) queryParams.add(new SearchCriteria("bugGroup", "=", bugGroup));

        List<BugDto> bugDtos = new ArrayList<>();

        // Find all bugs based upon filter criteria and set new updateBugEnabled
        bugService.findAll(queryParams).forEach(bugDto -> {
            bugDto.setBugEnabled(updateBugEnabled);
            bugDtos.add(bugService.update(bugDto));
        });

        return new ResponseEntity<>(bugDtos, HttpStatus.OK);
    }
    private void decryptBug(BugDto bugDto, String bugKey) {
        try {
            bugDto.setBugDescription(decrypt(bugDto.getBugDescription()));
            bugDto.setBugLocation(decrypt(bugDto.getBugLocation()));
            bugDto.setBugDescription(bugDto.getBugDescription());
            bugDto.setBugLocation(bugDto.getBugLocation());
        } catch (Exception e) {
            log.info("could not decipher bug: "+ bugKey);
        }
    }
    private static void initEncryptDecrypt() throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {
        ivParameterSpec = new IvParameterSpec(SECRET_KEY_1.getBytes("UTF-8"));
        secretKeySpec = new SecretKeySpec(SECRET_KEY_2.getBytes("UTF-8"), "AES");
        cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    }


    private static String decrypt(String encrypted) throws InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if (encrypted == null) {
            return null;
        }
        if (cipher == null || ivParameterSpec == null || secretKeySpec == null) {
            initEncryptDecrypt();
        }
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encrypted));
        return new String(decryptedBytes);
    }

}
