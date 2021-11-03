package io.github.jessicacarneiro.apisrest.interfaces;

import io.github.jessicacarneiro.apisrest.domain.Driver;
import io.github.jessicacarneiro.apisrest.infrastructure.DriverRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DriverAPITest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverAPI driverAPI;

    @Test
    void shouldReturnEmptyListIfNoDriversRegistered() {
        when(driverRepository.findAll()).thenReturn(Collections.emptyList());

        List<Driver> driversList = driverAPI.listDrivers();

        assertThat(Collections.emptyList()).isEqualTo(driversList);
    }

    @Test
    void shouldReturnListWithRegisteredDrivers() {
        List<Driver> expectedDriversList = new ArrayList<>();
        expectedDriversList.add(generateDriver(623866385000L, 1L,"João Costa" ));
        when(driverRepository.findAll()).thenReturn(expectedDriversList);

        List<Driver> actualDriversList = driverAPI.listDrivers();

        assertThat(expectedDriversList).isEqualTo(actualDriversList);
        verify(driverRepository).findAll();
    }

    @Test
    void shouldFindDriverById() {
        Driver expectedDriver = generateDriver(257014172000L, 2L,"Maria Almeida" );
        Long driverIdToSearch = expectedDriver.getId();
        when(driverRepository.findById(driverIdToSearch)).thenReturn(java.util.Optional.of(expectedDriver));

        Driver actualDriver = driverAPI.findDriver(driverIdToSearch);

        assertThat(actualDriver).isEqualTo(expectedDriver);
        verify(driverRepository).findById(driverIdToSearch);
    }

    @Test
    void shouldReturnIdIfSearchedDriverDoesNotExist() {
        long driverIdToSearch = 3L;
        when(driverRepository.findById(driverIdToSearch)).thenReturn(null);

        assertThatThrownBy(() -> driverAPI.findDriver(driverIdToSearch)).isInstanceOf(NullPointerException.class);
        verify(driverRepository).findById(driverIdToSearch);
    }

    @Test
    void shouldCreateANewDriver() {
        Driver expectedDriver = generateDriver(257014172000L, 3L,"Marcos Rocha" );
        when(driverRepository.save(expectedDriver)).thenReturn(expectedDriver);

        Driver createdDriver = driverAPI.createDriver(expectedDriver);

        assertThat(createdDriver).isEqualTo(expectedDriver);
        verify(driverRepository).save(expectedDriver);
    }

    private Driver generateDriver(long timestamp, long id, String name) {
        Driver driver = new Driver();

        driver.setId(id);
        driver.setName(name);
        driver.setDateOfBirth(new Date(timestamp));

        return driver;
    }
}