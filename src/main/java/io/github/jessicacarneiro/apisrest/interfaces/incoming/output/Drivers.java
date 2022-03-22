package io.github.jessicacarneiro.apisrest.interfaces.incoming.output;

import io.github.jessicacarneiro.apisrest.domain.Driver;
import java.util.List;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

@Getter
public class Drivers {

  private List<EntityModel<Driver>> drivers;

  private Link[] links;

  public Drivers(List<EntityModel<Driver>> content, Link... links) {
    this.drivers = content;
    this.links = links;
  }
}
