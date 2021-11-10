package io.github.jessicacarneiro.apisrest.domain;

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
