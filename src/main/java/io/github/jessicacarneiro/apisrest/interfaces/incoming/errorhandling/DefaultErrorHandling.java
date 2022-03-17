package io.github.jessicacarneiro.apisrest.interfaces.incoming.errorhandling;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultErrorHandling {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {

        List<ErrorData> messages = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ErrorData(fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ErrorResponse(messages);
    }
}
