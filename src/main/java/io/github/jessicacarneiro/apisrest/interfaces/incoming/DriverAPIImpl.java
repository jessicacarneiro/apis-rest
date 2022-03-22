package io.github.jessicacarneiro.apisrest.interfaces.incoming;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.github.jessicacarneiro.apisrest.domain.Driver;
import io.github.jessicacarneiro.apisrest.domain.DriverRepository;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.errorhandling.exceptions.UserNotFoundException;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.output.Drivers;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
@RequestMapping(path = "/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
public class DriverAPIImpl implements DriverAPI {

  private static final int PAGE_SIZE = 10;
  @Autowired
  private DriverRepository repository;

  @GetMapping
  public Drivers listDrivers(
      @RequestParam(name = "page", defaultValue = "0") int page) {
    Page<Driver> driverPage = repository.findAll(PageRequest.of(page, PAGE_SIZE));

    List<EntityModel<Driver>> driverList = new ArrayList<>();
    for (Driver driver : driverPage.getContent()) {
      driverList.add(EntityModel.of(driver));
    }

    Link lastPageLink = linkTo(
        methodOn(DriverAPIImpl.class)
            .listDrivers(driverPage.getTotalPages() - 1))
        .withRel("lastPage");

    return new Drivers(driverList, lastPageLink);
  }

  @GetMapping("/{id}")
  public Driver findDriver(@PathVariable("id") Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("Driver with id " + id + " was not found"));
  }

  @PostMapping
  public Driver createDriver(@RequestBody Driver driver) {
    return repository.save(driver);
  }

  @PatchMapping("/{id}")
  public Driver partiallyUpdateDriver(
      @PathVariable("id") long id,
      @RequestBody Driver driver
  ) {
    Driver foundDriver = findDriver(id);

    foundDriver.setName(Optional.ofNullable(driver.getName()).orElse(foundDriver.getName()));
    foundDriver.setDateOfBirth(
        Optional.ofNullable(driver.getDateOfBirth()).orElse(foundDriver.getDateOfBirth()));

    return repository.save(foundDriver);
  }

  @PutMapping("/{id}")
  public Driver fullyUpdateDriver(
      @PathVariable("id") Long id,
      @RequestBody Driver driver
  ) {
    Driver foundDriver = findDriver(id);

    foundDriver.setName(driver.getName());
    foundDriver.setDateOfBirth(driver.getDateOfBirth());

    return repository.save(foundDriver);
  }

  @DeleteMapping("/{id}")
  public void deleteDriver(@PathVariable("id") Long id) {
    repository.delete(findDriver(id));
  }
}
