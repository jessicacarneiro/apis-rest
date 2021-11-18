package io.github.jessicacarneiro.apisrest.interfaces.outcoming.output;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Position {
    private BigDecimal lat;
    private BigDecimal lon;
}
