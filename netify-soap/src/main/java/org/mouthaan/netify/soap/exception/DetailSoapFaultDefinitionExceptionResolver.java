package org.mouthaan.netify.soap.exception;

import org.mouthaan.netify.service.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import javax.xml.namespace.QName;
import java.util.Locale;

@Component
public class DetailSoapFaultDefinitionExceptionResolver extends SoapFaultMappingExceptionResolver {
    private static final QName CODE = new QName("code");
    private static final QName DESCRIPTION = new QName("description");

    private final MessageSource messageSource;

    @Autowired
    public DetailSoapFaultDefinitionExceptionResolver(MessageSource messageSource) {
        super();
        this.messageSource = messageSource;
    }

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        SoapFaultDetail detail = fault.addFaultDetail();
        Locale locale = LocaleContextHolder.getLocale();
        String code;
        String description;
        if (ex instanceof BaseException) {
            BaseException be = (BaseException)ex;
            String[] parameters = {messageSource.getMessage(be.getElementType(), null, locale), be.getParameters()};
            code = messageSource.getMessage(be.getResourceType(), null, locale);
            description = messageSource.getMessage(be.getResourceDescription().replaceAll(" ","."), parameters, locale);
        } else {
            code = ex.getClass().getSimpleName();
            description = ex.getMessage();
        }
        detail.addFaultDetailElement(CODE).addText(code);
        detail.addFaultDetailElement(DESCRIPTION).addText(description);
    }
}
