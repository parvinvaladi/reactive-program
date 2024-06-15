package com.programming.bookservice.exception.handler;

import com.programming.bookservice.dto.ResponseMessageDto;
import com.programming.bookservice.exception.ExceptionNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NotFoundHandler {

    @ExceptionHandler(value = ExceptionNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseMessageDto handle(ExceptionNotFound ex){
        return ResponseMessageDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .build();
    }
}
