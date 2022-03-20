package io.github.jessicacarneiro.apisrest.interfaces.incoming;

import io.github.jessicacarneiro.apisrest.domain.Driver;
import io.github.jessicacarneiro.apisrest.domain.DriverRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Service
@RestController
@RequestMapping(path = "/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
public class DriverAPIImpl implements DriverAPI {

    @Autowired
    private DriverRepository repository;

    @GetMapping
    public List<Driver> listDrivers() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Driver findDriver(@PathVariable("id") Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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
        foundDriver.setDateOfBirth(Optional.ofNullable(driver.getDateOfBirth()).orElse(foundDriver.getDateOfBirth()));

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
