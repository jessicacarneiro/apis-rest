package io.github.jessicacarneiro.apisrest.interfaces;

import io.github.jessicacarneiro.apisrest.domain.Driver;
import io.github.jessicacarneiro.apisrest.infrastructure.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class DriverAPI {

    @Autowired
    private DriverRepository repository;

    @GetMapping("/drivers")
    public List<Driver> listDrivers() {
        return repository.findAll();
    }

    @GetMapping("/drivers/{id}")
    public Driver findDriver(@PathVariable("id") Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/drivers")
    public Driver createDriver(@RequestBody Driver driver) {
        return repository.save(driver);
    }

    @PutMapping("/drivers/{id}")
    public Driver fullyUpdateDriver(@PathVariable("id") Long id, @RequestBody Driver driver) {
        Driver foundDriver = findDriver(id);

        foundDriver.setName(driver.getName());
        foundDriver.setDateOfBirth(driver.getDateOfBirth());

        return repository.save(foundDriver);
    }

    @PatchMapping("/drivers/{id}")
    public Driver partiallyUpdateDriver(@PathVariable("id") long id, @RequestBody Driver driver) {
        Driver foundDriver = findDriver(id);

        foundDriver.setName(Optional.ofNullable(driver.getName()).orElse(foundDriver.getName()));
        foundDriver.setDateOfBirth(Optional.ofNullable(driver.getDateOfBirth()).orElse(foundDriver.getDateOfBirth()));

        return repository.save(foundDriver);
    }
}
