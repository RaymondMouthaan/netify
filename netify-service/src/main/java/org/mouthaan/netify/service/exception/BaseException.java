package org.mouthaan.netify.service.exception;

public interface BaseException {
    String getElementType();

    String getParameters();

    //String getMessage();

    String getResourceType();

    String getResourceDescription();
}
