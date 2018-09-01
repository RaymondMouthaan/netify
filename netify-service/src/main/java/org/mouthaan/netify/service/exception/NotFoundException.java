package org.mouthaan.netify.service.exception;

public class NotFoundException extends RuntimeException implements BaseException {
    private final String RESOURCE_KEY_TYPE = "message.type.not.found";
    private final String RESOURCE_KEY_DESCRIPTION = "exception.not.found";

    private String type = "";
    private String parameters = "";
    private String message;

    public NotFoundException(String type, String parameters) {
        this.type = type;
        this.parameters = parameters;
        this.message = "Element '" + type + "' with parameters '" + parameters + "' not found";
    }

    @Override
    public String getElementType() {
        return type;
    }

    @Override
    public String getParameters() {
        return parameters;
    }

    //@Override
    //public String getMessage() {
    //    return message;
    //}

    @Override
    public String getResourceType() {
        return RESOURCE_KEY_TYPE;
    }

    @Override
    public String getResourceDescription() {
        return RESOURCE_KEY_DESCRIPTION;
    }
}
