package com.bismark.serviceboilerplate.controller;

import com.bismark.serviceboilerplate.dto.ErrorDto;
import com.bismark.serviceboilerplate.error.UpdateUserException;
import com.bismark.serviceboilerplate.error.UpdateUsernameException;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private ErrorDto buildError(Exception exception) {
        String errorCode = "errUnknown";
        String errorMessage = "Unknown error";
        if (exception.getClass() == UpdateUsernameException.class) {
            errorCode = "ErrUpdateUsername";
            errorMessage = "Update username is forbidden";
        } else if (exception.getClass() == UpdateUserException.class) {
            errorCode = "ErrUpdateUser";
            errorMessage = "Update user is failed";
        }

        return new ErrorDto(errorMessage, errorCode);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ErrorDto> handleAllExceptions(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildError(exception));
    }

    @ExceptionHandler(UpdateUsernameException.class)
    public ResponseEntity<@NonNull ErrorDto> handleUpdateUsernameException(UpdateUsernameException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildError(exception));
    }
}
