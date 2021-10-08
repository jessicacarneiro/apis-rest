package io.github.jessicacarneiro.apisrest.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Driver {

    @Id
    Long id;
    String name;
    Date dateOfBirth;

}
