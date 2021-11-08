package io.github.jessicacarneiro.apisrest.interfaces;

import io.github.jessicacarneiro.apisrest.domain.Passenger;
import io.github.jessicacarneiro.apisrest.infrastructure.PassengerRepository;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PassengerAPIIntTest {

    @Autowired
    private PassengerRepository repository;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
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
    public void getPassengerWhenSearchingDriverById() throws Exception {
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

    private Passenger generatePassenger(Long id, String name) {
        Passenger passenger = new Passenger();

        passenger.setId(id);
        passenger.setName(name);

        return passenger;
    }
}