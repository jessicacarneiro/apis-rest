package io.github.jessicacarneiro.apisrest.services;

import io.github.jessicacarneiro.apisrest.domain.Passenger;
import io.github.jessicacarneiro.apisrest.domain.TravelRequest;
import io.github.jessicacarneiro.apisrest.domain.PassengerRepository;
import io.github.jessicacarneiro.apisrest.domain.TravelRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
public class TravelServiceTestIT {

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TravelRequestRepository travelRequestRepository;

    @Autowired
    private final TravelService service = new TravelService(travelRequestRepository, null, null);

    @Test
    void shouldCreateTravelRequestWithCorrectFields() {
        String passengerName = "Lucas Mendonça";
        String origin = "Avenida Cristiano Machado, 890";
        String destination = "Avenida Arthur Bernardes, 25";

        Passenger passenger = generatePassenger(passengerName);
        passenger = passengerRepository.save(passenger);
        TravelRequest expectedTravelRequest = generateTravelRequest(passenger, origin, destination);

        TravelRequest actualTravelRequest = service.saveTravelRequest(expectedTravelRequest);

        assertThat(actualTravelRequest.getPassenger().getName()).isEqualTo(passengerName);
        assertThat(actualTravelRequest.getOrigin()).isEqualTo(origin);
        assertThat(actualTravelRequest.getDestination()).isEqualTo(destination);
        assertThat(actualTravelRequest.getCreationDate()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenCreatingATravelRequestAndPassengerDoesNotExist() {
        Passenger passenger = generatePassenger("Juliana Carneiro");
        TravelRequest travelRequest =
                generateTravelRequest(passenger, "Rua do Rosário 123", "Rua Machado 456");

        assertThatThrownBy(() -> service.saveTravelRequest(travelRequest)).isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    private Passenger generatePassenger(String passengerName) {
        Passenger passenger = new Passenger();
        passenger.setName(passengerName);

        return passenger;
    }

    private TravelRequest generateTravelRequest(Passenger passenger, String origin, String destination) {
        TravelRequest travelRequest = new TravelRequest();
        travelRequest.setPassenger(passenger);
        travelRequest.setOrigin(origin);
        travelRequest.setDestination(destination);

        return travelRequest;
    }


}
