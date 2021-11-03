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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

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
        String dateOfBirth = "2000-05-23T09:01:02.000+00:00";
        Driver driver = generateDriver(dateOfBirth, 2L, "Maria Almeida");
        repository.save(driver);

        mvc.perform(MockMvcRequestBuilders
                        .get("/drivers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(driver.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dateOfBirth").value(dateOfBirth))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(driver.getName()));
    }

    @Test
    public void getDriverWhenSearchingDriverById() throws Exception {
        String dateOfBirth = "1978-12-05T19:23:56.000+00:00";
        Driver driver = generateDriver(dateOfBirth, 1L, "João Costa");
        repository.save(driver);

        mvc.perform(MockMvcRequestBuilders
                        .get("/drivers/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(driver.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfBirth").value(dateOfBirth))
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
    public void getNotFoundWhenTryingToGetDriverThatDoesNotExist() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/drivers/5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private Driver generateDriver(String date, long id, String name) throws ParseException {
        Driver driver = new Driver();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        driver.setId(id);
        driver.setName(name);
        driver.setDateOfBirth(dateFormat.parse(date));

        return driver;
    }
}
