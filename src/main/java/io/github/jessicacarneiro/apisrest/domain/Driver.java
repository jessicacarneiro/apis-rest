package io.github.jessicacarneiro.apisrest.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
public class Driver {

    @Id
    @GeneratedValue
    Long id;
    String name;
    LocalDate dateOfBirth;

}
