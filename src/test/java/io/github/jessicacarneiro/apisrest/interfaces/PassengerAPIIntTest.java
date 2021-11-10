package io.github.jessicacarneiro.apisrest.interfaces;

import io.github.jessicacarneiro.apisrest.domain.Passenger;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PassengerAPIIntTest {

    @Autowired
    private PassengerRepository repository;

    @Autowired
    private TravelRequestRepository travelRequestRepository;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        travelRequestRepository.deleteAll();
        repository.deleteAll();
    }

    @Test
    void getEmptyListWhenNoPassengerIsAvailable() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/passengers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    void getListWithPassengersWhenAvailable() throws Exception {
        Passenger passenger = generatePassenger(1L, "Jorge Amado");
        passenger = repository.save(passenger);

        mvc.perform(MockMvcRequestBuilders
                        .get("/passengers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(passenger.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(passenger.getName()));
    }

    @Test
    public void getPassengerWhenSearchingPassengerById() throws Exception {
        Passenger passenger = generatePassenger(2L, "Gustavo Lima");
        Passenger passengerSaved = repository.save(passenger);

        mvc.perform(MockMvcRequestBuilders
                        .get("/passengers/" + passengerSaved.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(passengerSaved.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(passengerSaved.getName()));
    }

    @Test
    public void getNotFoundWhenTryingToGetPassengerThatDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/passengers/5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createNewPassengerWithSuccess() throws Exception {
        Passenger passenger = generatePassenger(3L, "Marília Mendonça");
        String requestBody = generatePostBody(passenger);

        mvc.perform(MockMvcRequestBuilders
                        .post("/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(passenger.getName()));
    }

    @Test
    void shouldReturnBadRequestIfBodyNotProvidedWhenCreatingANewPassenger() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/passengers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void fullyUpdateExistingPassenger() throws Exception {
        Passenger passenger = generatePassenger(4L, "Marília Mendonça");
        Passenger existingPassenger = repository.save(passenger);

        passenger.setName("Rainha da Sofrência");
        String requestBody = generatePostBody(passenger);

        mvc.perform(MockMvcRequestBuilders
                        .put("/passengers/" + existingPassenger.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(existingPassenger.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(passenger.getName()));
    }

    @Test
    void shouldReturnBadRequestIfFullyUpdatingPassengerWithoutABody() throws Exception {
        Passenger passenger = generatePassenger(8L, "Joaquim Ferreira");
        Passenger existingPassenger = repository.save(passenger);

        mvc.perform(MockMvcRequestBuilders
                        .put("/passengers/" + existingPassenger.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundIfTryingToFullyUpdateNonExistingPassenger() throws Exception {
        Passenger passenger = generatePassenger(9L, "Leonardo Amaral");
        String requestBody = generatePostBody(passenger);

        mvc.perform(MockMvcRequestBuilders
                        .put("/passengers/" + passenger.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void partiallyUpdateExistingPassenger() throws Exception {
        Passenger passenger = generatePassenger(10L, "Joca Soares");
        Passenger existingPassenger = repository.save(passenger);

        passenger.setName("Joca Severino");
        String requestBody = generatePostBody(passenger);

        mvc.perform(MockMvcRequestBuilders
                        .patch("/passengers/" + existingPassenger.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(existingPassenger.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(passenger.getName()));
    }

    @Test
    void shouldReturnBadRequestIfPartiallyUpdatingPassengerWithoutABody() throws Exception {
        Passenger passenger = generatePassenger(1L, "Manoel Bandeira");
        Passenger existingPassenger = repository.save(passenger);

        mvc.perform(MockMvcRequestBuilders
                        .patch("/passengers/" + existingPassenger.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundIfTryingToPartiallyUpdateNonExistingPassenger() throws Exception {
        Passenger passenger = generatePassenger(2L, "Daniel Amado");
        String requestBody = generatePostBody(passenger);

        mvc.perform(MockMvcRequestBuilders
                        .patch("/passengers/" + passenger.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeletePassengerWithSuccess() throws Exception {
        Passenger passenger = generatePassenger(12L, "Alessandra Dias");
        Passenger savedPassenger = repository.save(passenger);

        mvc.perform(MockMvcRequestBuilders
                        .delete("/passengers/" + savedPassenger.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());

        assertThat(repository.findById(savedPassenger.getId())).isEmpty();
    }

    @Test
    public void shouldReturnNotFoundWhenTryingToDeleteNonExistentPassenger() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .delete("/passengers/5"))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    private Passenger generatePassenger(Long id, String name) {
        Passenger passenger = new Passenger();

        passenger.setId(id);
        passenger.setName(name);

        return passenger;
    }

    private String generatePostBody(Passenger passenger) {
        return "{\"name\":" + "\"" + passenger.getName() + "\"" + "}";
    }
}