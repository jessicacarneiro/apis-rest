package io.github.jessicacarneiro.apisrest.interfaces.incoming.errorhandling;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {

    List<ErrorData> errors;
}
