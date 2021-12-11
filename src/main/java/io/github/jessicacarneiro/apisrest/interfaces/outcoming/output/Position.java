package io.github.jessicacarneiro.apisrest.interfaces.outcoming.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Position {
    private double lat;
    private double lon;
}
