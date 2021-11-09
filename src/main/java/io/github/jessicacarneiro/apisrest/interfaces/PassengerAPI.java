package io.github.jessicacarneiro.apisrest.interfaces;

import io.github.jessicacarneiro.apisrest.domain.Passenger;
import io.github.jessicacarneiro.apisrest.infrastructure.PassengerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Service
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class PassengerAPI {

    @Autowired
    private PassengerRepository repository;

    @GetMapping("/passengers")
    public List<Passenger> listPassengers() {
        return repository.findAll();
    }

    @GetMapping("/passengers/{id}")
    public Passenger findPassenger(@PathVariable("id") Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/passengers")
    public Passenger createPassenger(@RequestBody Passenger passenger) {
        return repository.save(passenger);
    }

    @PutMapping("/passengers/{id}")
    public Passenger fullyUpdatePassenger(@PathVariable("id") Long id, @RequestBody Passenger passenger) {
        Passenger foundPassenger = findPassenger(id);

        foundPassenger.setName(passenger.getName());

        return repository.save(foundPassenger);
    }
}
