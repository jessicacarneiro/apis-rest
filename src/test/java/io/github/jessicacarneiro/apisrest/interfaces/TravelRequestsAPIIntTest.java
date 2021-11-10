package io.github.jessicacarneiro.apisrest.interfaces;

import io.github.jessicacarneiro.apisrest.domain.Passenger;
import io.github.jessicacarneiro.apisrest.infrastructure.PassengerRepository;
import io.github.jessicacarneiro.apisrest.infrastructure.TravelRequestRepository;
import io.github.jessicacarneiro.apisrest.interfaces.input.TravelRequestInput;
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

        TravelRequestInput travelRequest = generateTravelRequest(passenger, "Belo Horizonte", "Juiz de Fora");
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

    private TravelRequestInput generateTravelRequest(Passenger passenger, String origin, String destination) {
        TravelRequestInput travelRequest = new TravelRequestInput();

        travelRequest.setPassengerId(passenger.getId());
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

    private String generatePostBody(TravelRequestInput travelRequest) {
        return String.format("{\"passengerId\": %d, " +
                        "\"origin\": \"%s\", \"destination\": \"%s\" }",
                travelRequest.getPassengerId(),
                travelRequest.getOrigin(),
                travelRequest.getDestination()
        );
    }
}
