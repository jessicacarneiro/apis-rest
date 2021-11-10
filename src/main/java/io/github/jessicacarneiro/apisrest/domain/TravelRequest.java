package io.github.jessicacarneiro.apisrest.domain;

import javax.persistence.Entity;
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
}
