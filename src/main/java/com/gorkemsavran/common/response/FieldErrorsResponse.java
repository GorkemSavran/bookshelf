package com.gorkemsavran.common.response;

import java.util.List;

public class FieldErrorsResponse extends MessageResponse {

    private final List<String> fieldErrors;

    public FieldErrorsResponse(String message, MessageType messageType, List<String> fieldErrors) {
        super(message, messageType);
        this.fieldErrors = fieldErrors;
    }

    public List<String> getFieldErrors() {
        return fieldErrors;
    }
}
