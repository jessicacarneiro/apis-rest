package io.github.jessicacarneiro.apisrest.interfaces.incoming.errorhandling.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
