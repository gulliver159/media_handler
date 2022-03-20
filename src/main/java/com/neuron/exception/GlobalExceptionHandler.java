package com.neuron.exception;

import io.swagger.model.AttrError;
import io.swagger.model.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServerException.class)
    @ResponseBody
    public Error handleServerException(ServerException ex) {
        return ex.getError();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Error handleServerException(MethodArgumentNotValidException ex) {
        Error error = new Error()
                .code("validationError")
                .message("Parameter validation error");

        List<AttrError> attrErrors = Optional.of(ex.getFieldErrors())
                .orElseGet(Collections::emptyList).stream()
                .map(fieldError -> new AttrError()
                        .attr(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                )
                .collect(Collectors.toList());

        error.setAttrErrors(attrErrors);
        return error;
    }
}
