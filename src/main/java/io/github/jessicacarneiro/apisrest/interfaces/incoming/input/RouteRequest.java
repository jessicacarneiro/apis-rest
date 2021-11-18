package io.github.jessicacarneiro.apisrest.interfaces.incoming.input;

import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.Position;
import lombok.Data;

@Data
public class RouteRequest {
    Position origin;
    Position destination;
}
