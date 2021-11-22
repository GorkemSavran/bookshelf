package com.gorkemsavran.common.response;

public class MessageResponse {

    private final String message;

    private final MessageType messageType;

    public MessageResponse(String message, MessageType messageType) {
        this.message = message;
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public MessageType getMessageType() {
        return messageType;
    }
}
