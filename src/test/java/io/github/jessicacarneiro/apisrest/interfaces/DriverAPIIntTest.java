package io.github.jessicacarneiro.apisrest.interfaces;

import io.github.jessicacarneiro.apisrest.domain.Driver;
import io.github.jessicacarneiro.apisrest.infrastructure.DriverRepository;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DriverAPIIntTest {

    @Autowired
    private DriverRepository repository;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    public void getEmptyListWhenNoDriverIsAvailable() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/drivers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    public void getListWithAllDrivers() throws Exception {
        LocalDate dateOfBirth = LocalDate.of(1987, 3, 9);
        Driver driver = generateDriver(2L, "Maria Almeida", dateOfBirth);
        repository.save(driver);

        mvc.perform(MockMvcRequestBuilders
                        .get("/drivers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(driver.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dateOfBirth").value(dateOfBirth.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(driver.getName()));
    }

    @Test
    public void getDriverWhenSearchingDriverById() throws Exception {
        LocalDate dateOfBirth = LocalDate.of(1975, 12, 21);
        Driver driver = generateDriver(1L, "João Costa", dateOfBirth);
        repository.save(driver);

        mvc.perform(MockMvcRequestBuilders
                        .get("/drivers/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(driver.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value(dateOfBirth.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(driver.getName()));
    }

    @Test
    public void getNotFoundWhenTryingToGetDriverThatDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/drivers/5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateNewDriverWithSuccess() throws Exception {
        LocalDate dateOfBirth = LocalDate.of(1975, 12, 21);
        Driver driver = generateDriver(1L, "João Costa", dateOfBirth);
        String requestBody = generatePostBody(driver);

        mvc.perform(MockMvcRequestBuilders
                        .post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnBadRequestIfNoBodySentToCreateDriver() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/drivers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private Driver generateDriver(long id, String name, LocalDate dateOfBirth) {
        Driver driver = new Driver();

        driver.setId(id);
        driver.setName(name);
        driver.setDateOfBirth(dateOfBirth);

        return driver;
    }

    private String generatePostBody(Driver driver) {
        return "{\"id\":" + driver.getId() + "," +
                "\"name\":" + "\"" + driver.getName() + "\"" + "," +
                "\"dateOfBirth\":" + "\"" + driver.getDateOfBirth().toString() + "\"" + "}";
    }
}
