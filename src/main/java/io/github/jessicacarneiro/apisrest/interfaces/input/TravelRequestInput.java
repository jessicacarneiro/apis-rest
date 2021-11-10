package io.github.jessicacarneiro.apisrest.interfaces.input;

import lombok.Data;

@Data
public class TravelRequestInput {

    Long passengerId;
    String origin;
    String destination;
}
