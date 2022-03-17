package io.github.jessicacarneiro.apisrest.interfaces.incoming.input;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TravelRequestInput {

    @NotNull
    Long passengerId;
    String origin;
    String destination;
}
