package io.github.jessicacarneiro.apisrest.interfaces.incoming.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorData {

    private final String message;
}
