package org.mouthaan.netify.rest.exception;

import org.mouthaan.netify.service.BugService;
import org.mouthaan.netify.service.dto.MessageDto;
import org.mouthaan.netify.service.exception.AlreadyExistException;
import org.mouthaan.netify.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
@Component
public class ResponseExceptionHandler {

    private final MessageSource messageSource;
    private final BugService bugService;

    @Autowired
    public ResponseExceptionHandler(MessageSource messageSource, BugService bugService) {
        super();
        this.messageSource = messageSource;
        this.bugService = bugService;
    }

    @ExceptionHandler(value = AlreadyExistException.class)
    public ResponseEntity<MessageDto> handleAlreadyExistException(final AlreadyExistException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String[] parameters = {messageSource.getMessage(ex.getElementType(), null, locale), ex.getParameters()};

        MessageDto response = new MessageDto();
        response.setMessageType(messageSource.getMessage(ex.getResourceType(), null, locale));
        response.setMessageDescription(messageSource.getMessage(ex.getResourceDescription().replaceAll(" ", "."), parameters, locale));
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<MessageDto> handleValidationException(final MethodArgumentNotValidException ex) {
        Locale locale = LocaleContextHolder.getLocale();

        MessageDto response = new MessageDto();
        response.setMessageType(messageSource.getMessage("message.type.bad.request", null, locale));
        response.setMessageDescription(messageSource.getMessage(ex.getBindingResult().getFieldError().getDefaultMessage().replaceAll(" ", "."), null, locale));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<MessageDto> handleNotFoundException(NotFoundException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String[] parameters = {messageSource.getMessage(ex.getElementType(), null, locale), ex.getParameters()};

        MessageDto response = new MessageDto();
        response.setMessageType(messageSource.getMessage(ex.getResourceType(), null, locale));
        response.setMessageDescription(messageSource.getMessage(ex.getResourceDescription().replaceAll(" ", "."), parameters, locale));
        if (bugService.findByBugKey("bug_205").getBugEnabled()) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.PARTIAL_CONTENT);
    }

}
