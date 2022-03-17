package io.github.jessicacarneiro.apisrest.interfaces.incoming.input;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TravelRequestInput {

    @NotNull(message = "Field passengerId should not be null")
    Long passengerId;

    @NotEmpty(message = "Field origin should should not be empty")
    String origin;

    @NotEmpty(message = "Field destination should should not be empty")
    String destination;
}
