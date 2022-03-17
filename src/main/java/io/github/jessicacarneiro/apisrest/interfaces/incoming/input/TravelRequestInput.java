package io.github.jessicacarneiro.apisrest.interfaces.incoming.input;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TravelRequestInput {

    @NotNull(message = "passengerId should not be null")
    Long passengerId;
    String origin;
    String destination;
}
