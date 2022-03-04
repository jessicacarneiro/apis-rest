package io.github.jessicacarneiro.apisrest.interfaces.incoming;

import io.github.jessicacarneiro.apisrest.domain.Passenger;
import io.github.jessicacarneiro.apisrest.domain.PassengerRepository;
import java.util.List;
import java.util.Optional;
import javax.annotation.security.RolesAllowed;
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
@RequestMapping(path = "/passengers", produces = MediaType.APPLICATION_JSON_VALUE)
public class PassengerAPI {

    @Autowired
    private PassengerRepository repository;

    @GetMapping
    public List<Passenger> listPassengers() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Passenger findPassenger(@PathVariable("id") Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @RolesAllowed("ROLE_ADMIN")
    public Passenger createPassenger(@RequestBody Passenger passenger) {
        return repository.save(passenger);
    }

    @PutMapping("/{id}")
    public Passenger fullyUpdatePassenger(@PathVariable("id") Long id, @RequestBody Passenger passenger) {
        Passenger foundPassenger = findPassenger(id);

        foundPassenger.setName(passenger.getName());

        return repository.save(foundPassenger);
    }

    @PatchMapping("/{id}")
    public Passenger partiallyUpdatePassenger(@PathVariable("id") Long id, @RequestBody Passenger passenger) {
        Passenger foundPassenger = findPassenger(id);

        foundPassenger.setName(Optional.ofNullable(passenger.getName()).orElse(foundPassenger.getName()));

        return repository.save(foundPassenger);
    }

    @DeleteMapping("/{id}")
    public void deletePassenger(@PathVariable("id") Long id) {
        repository.delete(findPassenger(id));
    }
}
