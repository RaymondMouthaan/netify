package org.mouthaan.netify.service.exception;

public class AlreadyExistException extends RuntimeException implements BaseException  {
    private final String RESOURCE_KEY_TYPE = "message.type.duplicate";
    private final String RESOURCE_KEY_DESCRIPTION = "exception.already.exists";

    private String type = "";
    private String parameters = "";


    public AlreadyExistException(String type, String parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    @Override
    public String getElementType() {
        return type;
    }

    @Override
    public String getParameters(){
        return this.parameters;
    }


//    @Override
//    public String getMessage() {
//        return type + " with value '" + parameters + "' already exists.";
//    }

    @Override
    public String getResourceType() {
        return RESOURCE_KEY_TYPE;
    }

    @Override
    public String getResourceDescription() {
        return RESOURCE_KEY_DESCRIPTION;
    }
}
