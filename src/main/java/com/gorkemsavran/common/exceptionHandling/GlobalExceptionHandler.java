package com.gorkemsavran.common.exceptionHandling;

import com.gorkemsavran.common.response.FieldErrorsResponse;
import com.gorkemsavran.common.response.MessageResponse;
import com.gorkemsavran.common.response.MessageType;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<MessageResponse> handleConflict(EntityNotFoundException ex) {
        return ResponseEntity.status(404).body(new MessageResponse(ex.getMessage(), MessageType.ERROR));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<MessageResponse> handleFieldValidationConflicts(MethodArgumentNotValidException ex) {
        List<String> fieldErrors = extractFieldErrors(ex);
        return ResponseEntity.status(400).body(new FieldErrorsResponse("There are some errors on field validation", MessageType.ERROR, fieldErrors));
    }

    private List<String> extractFieldErrors(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
    }

}
