package io.github.jessicacarneiro.apisrest.interfaces.incoming.output;

import io.github.jessicacarneiro.apisrest.domain.TravelRequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
@Schema(description = "Representes a created travel requested")
public class TravelRequestOutput {

    @Schema(description = "Travel request ID")
    Long id;

    @Schema(description = "Origin (address) for the travel request")
    String origin;

    @Schema(description = "Destination (address) for the travel request")
    String destination;

    @Schema(description = "Travel request status. Recently created requests start with the status of CREATED")
    TravelRequestStatus status;

    @Schema(description = "Travel request creating date")
    OffsetDateTime creationDate;
}
