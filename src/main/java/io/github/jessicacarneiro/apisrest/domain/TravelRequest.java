package io.github.jessicacarneiro.apisrest.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
@Schema(description = "Represents a travel request created for a passenger in the platform")
public class TravelRequest {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    Passenger passenger;

    String origin;
    String destination;
    OffsetDateTime creationDate;

    @Enumerated(EnumType.STRING)
    TravelRequestStatus status;
}
