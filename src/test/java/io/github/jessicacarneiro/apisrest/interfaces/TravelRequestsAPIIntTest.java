package io.github.jessicacarneiro.apisrest.interfaces;

import io.github.jessicacarneiro.apisrest.domain.Passenger;
import io.github.jessicacarneiro.apisrest.domain.TravelRequest;
import io.github.jessicacarneiro.apisrest.infrastructure.PassengerRepository;
import io.github.jessicacarneiro.apisrest.infrastructure.TravelRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TravelRequestsAPIIntTest {

    @Autowired
    private TravelRequestRepository travelRequestRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        passengerRepository.deleteAll();
        travelRequestRepository.deleteAll();
    }

    @Test
    void createTravelRequestWithSuccess() throws Exception {
        Passenger passenger = generatePassenger(1L, "José Vieira");
        passenger = passengerRepository.save(passenger);

        TravelRequest travelRequest = generateTravelRequest(passenger, "Belo Horizonte", "Juiz de Fora");
        String requestBody = generatePostBody(travelRequest);

        mvc.perform(MockMvcRequestBuilders
                        .post("/travelRequests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void returnBadRequestIfBodyNotSentToCreateTravelRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/travelRequests")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private TravelRequest generateTravelRequest(Passenger passenger, String origin, String destination) {
        TravelRequest travelRequest = new TravelRequest();

        travelRequest.setPassenger(passenger);
        travelRequest.setOrigin(origin);
        travelRequest.setDestination(destination);

        return travelRequest;
    }

    private Passenger generatePassenger(Long id, String name) {
        Passenger passenger = new Passenger();

        passenger.setId(id);
        passenger.setName(name);

        return passenger;
    }

    private String generatePostBody(TravelRequest travelRequest) {
        return String.format("{\"passenger\": { \"id\": %d, \"name\": \"%s\" }, " +
                        "\"origin\": \"%s\", \"destination\": \"%s\" }",
                travelRequest.getPassenger().getId(),
                travelRequest.getPassenger().getName(),
                travelRequest.getOrigin(),
                travelRequest.getDestination()
        );
    }
}
