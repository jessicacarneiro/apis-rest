package io.github.jessicacarneiro.apisrest.interfaces.mapping;

import io.github.jessicacarneiro.apisrest.domain.Passenger;
import io.github.jessicacarneiro.apisrest.domain.TravelRequest;
import io.github.jessicacarneiro.apisrest.infrastructure.PassengerRepository;
import io.github.jessicacarneiro.apisrest.interfaces.input.TravelRequestInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class TravelRequestMapper {

    @Autowired
    private PassengerRepository passengerRepository;

    public TravelRequest map(TravelRequestInput input) {
        Passenger passenger = passengerRepository.findById(input.getPassengerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        TravelRequest travelRequest = new TravelRequest();
        travelRequest.setPassenger(passenger);
        travelRequest.setOrigin(input.getOrigin());
        travelRequest.setDestination(input.getDestination());

        return travelRequest;
    }
}
