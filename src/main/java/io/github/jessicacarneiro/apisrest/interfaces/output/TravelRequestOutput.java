package io.github.jessicacarneiro.apisrest.interfaces.output;

import io.github.jessicacarneiro.apisrest.domain.TravelRequestStatus;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class TravelRequestOutput {

    Long id;
    String origin;
    String destination;
    TravelRequestStatus status;
    OffsetDateTime creationDate;
}
