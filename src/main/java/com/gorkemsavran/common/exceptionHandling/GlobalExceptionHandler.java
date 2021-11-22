package com.gorkemsavran.common.exceptionHandling;

import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.common.response.MessageType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<MessageResponse> handleConflict(EntityNotFoundException ex) {
        return ResponseEntity.status(404).body(new MessageResponse("Entity not found!", MessageType.ERROR));
    }

}
