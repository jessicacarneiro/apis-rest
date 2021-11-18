package io.github.jessicacarneiro.apisrest.interfaces.outcoming.output;

import java.util.List;
import lombok.Data;

@Data
public class AddressResponse {
    private List<Result> results;
}

