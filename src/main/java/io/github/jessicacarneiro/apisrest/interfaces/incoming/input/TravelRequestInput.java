package io.github.jessicacarneiro.apisrest.interfaces.incoming.input;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Travel request submitted to be created in the platform")
public class TravelRequestInput {

    @NotNull
    @Schema(description = "Passenger ID for which the travel request will be created")
    Long passengerId;

    @NotEmpty
    @Schema(description = "Passenger's current address")
    String origin;

    @NotEmpty
    @Schema(description = "Passenger's final destination")
    String destination;
}
