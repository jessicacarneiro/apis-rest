package io.github.jessicacarneiro.apisrest.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Schema(description = "Represents a user of the platform")
public class User {

    @Id
    @GeneratedValue
    Long id;

    @Column(unique = true)
    @Size(min = 5, max = 20)
    String username;

    @Size(min = 10, max = 255)
    String password;

    @Schema(description = "States if user is active or not")
    Boolean enabled;

    @ElementCollection
    List<String> roles;
}
