package io.github.jessicacarneiro.apisrest.interfaces.incoming.errorhandling;

import io.github.jessicacarneiro.apisrest.interfaces.incoming.errorhandling.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Collections;
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
    @ApiResponses(
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            ))
    )
    public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {

        List<ErrorData> messages = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::getMessage)
                .collect(Collectors.toList());

        return new ErrorResponse(messages);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(UserNotFoundException exception) {
        ErrorData error = new ErrorData(exception.getMessage());
        return new ErrorResponse(Collections.singletonList(error));
    }

    private ErrorData getMessage(FieldError fieldError) {
        return new ErrorData(messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()));
    }
}
