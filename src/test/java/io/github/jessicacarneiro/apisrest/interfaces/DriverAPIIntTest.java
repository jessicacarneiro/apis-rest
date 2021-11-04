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
        Driver driver = generateDriver("Maria Almeida", dateOfBirth);
        Driver driverSaved = repository.save(driver);

        mvc.perform(MockMvcRequestBuilders
                        .get("/drivers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(driverSaved.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dateOfBirth").value(driverSaved.getDateOfBirth().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(driverSaved.getName()));
    }

    @Test
    public void getDriverWhenSearchingDriverById() throws Exception {
        LocalDate dateOfBirth = LocalDate.of(1975, 12, 21);
        Driver driver = generateDriver("João Costa", dateOfBirth);
        Driver driverSaved = repository.save(driver);

        mvc.perform(MockMvcRequestBuilders
                        .get("/drivers/" + driverSaved.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(driverSaved.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value(driverSaved.getDateOfBirth().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(driverSaved.getName()));
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
        Driver driver = generateDriver("João Costa", dateOfBirth);
        String requestBody = generatePostBody(driver);

        mvc.perform(MockMvcRequestBuilders
                        .post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value(driver.getDateOfBirth().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(driver.getName()));
    }

    @Test
    public void shouldReturnBadRequestIfNoBodySentToCreateDriver() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .post("/drivers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFullyUpdateADriverWithSuccess() throws Exception {
        Driver driver = generateDriver("João Costa", LocalDate.of(1975, 12, 21));
        Driver driverSaved = repository.save(driver);

        driver.setName("Marcelo Lima");
        String requestBody = generatePostBodyWithoutDateOfBirth(driver);

        mvc.perform(MockMvcRequestBuilders
                        .put("/drivers/" + driverSaved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(driverSaved.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(driver.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").doesNotExist());
    }

    @Test
    public void shouldReturnNotFoundWhenTryingToFullyUpdateNonExistentDriver() throws Exception {
        Driver driver = generateDriver("Heloisa Menezes", LocalDate.of(1953, 2, 12));
        String requestBody = generatePostBody(driver);

        mvc.perform(MockMvcRequestBuilders
                        .put("/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnBadRequestIfNoBodySentToFullyUpdateDriver() throws Exception {
        Driver driver = generateDriver("João Costa", LocalDate.of(1975, 12, 21));
        Driver driverSaved = repository.save(driver);

        driver.setName("Marcelo Lima");
        driver.setDateOfBirth(LocalDate.of(1995, 7, 30));

        mvc.perform(MockMvcRequestBuilders
                        .put("/drivers/" + driverSaved.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private Driver generateDriver(String name, LocalDate dateOfBirth) {
        Driver driver = new Driver();

        driver.setName(name);
        driver.setDateOfBirth(dateOfBirth);

        return driver;
    }

    private String generatePostBody(Driver driver) {
        return "{\"name\":" + "\"" + driver.getName() + "\"" + "," +
                "\"dateOfBirth\":" + "\"" + driver.getDateOfBirth().toString() + "\"" + "}";
    }

    private String generatePostBodyWithoutDateOfBirth(Driver driver) {
        return "{\"name\":" + "\"" + driver.getName() + "\"" + "}";
    }
}
