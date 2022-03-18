package io.github.jessicacarneiro.apisrest.interfaces.incoming.errorhandling;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultErrorHandling {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {

        List<ErrorData> messages = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::getMessage)
                .collect(Collectors.toList());

        return new ErrorResponse(messages);
    }

    private ErrorData getMessage(FieldError fieldError) {
        return new ErrorData(messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()));
    }
}
