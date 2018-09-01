package org.mouthaan.netify.rest.utils;

//import nl.ocwduo.testautomatisering.netflux.schema.GetHandleSuccessMessageResponse;


import org.mouthaan.netify.service.dto.MessageDto;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public class Util {
    private static Locale locale = LocaleContextHolder.getLocale();

    /** TODO volgens mij zijn er meer types nodig? deze heeft specifiek nog een Long nodig namelijk
     * Create successful messages according to their element type and the MessageSource Bundle
     *
     * @param messageSource         The Resource Bundle
     * @param messageType           The type of the object (Actor,Movie,Genre)
     * @param messageDescription    The message itself
     * @param id                    number to be embedded in the message
     * @return                      a filled response message
     */
    public static ResponseEntity<MessageDto> handleSuccessAcceptance(MessageSource messageSource, String messageType, String messageDescription, Long id) {
        String[] parameters = {messageSource.getMessage(messageType, null, locale), Long.toString(id)};
        return handleSuccessAcceptance(messageSource, messageDescription, parameters);
    }

    public static ResponseEntity<MessageDto> handleSuccessAcceptance(MessageSource messageSource, String messageDescription, String[] parameters) {
        MessageDto response = new MessageDto();
        response.setMessageType(messageSource.getMessage("message.type.success", null, locale));
        response.setMessageDescription(messageSource.getMessage(messageDescription, parameters, locale));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
