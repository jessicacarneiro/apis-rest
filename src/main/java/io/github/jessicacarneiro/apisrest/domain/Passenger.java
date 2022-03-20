package io.github.jessicacarneiro.apisrest.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Schema(description = "Represents a passenger in the platform")
public class Passenger {

    @Id
    @GeneratedValue
    private Long id;

    @Schema(description = "Passenger's name")
    @Size(min = 5, max = 255)
    String name;
}
