package io.github.jessicacarneiro.apisrest.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Schema(description = "Represents a driver in the platform")
public class Driver {

    @Id
    @GeneratedValue
    Long id;

    @Schema(description = "Driver's name")
    @Size(min = 5, max = 255)
    String name;

    @Schema(description = "Driver's date of birth")
    LocalDate dateOfBirth;

}
