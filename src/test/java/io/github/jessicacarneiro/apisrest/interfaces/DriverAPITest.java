package io.github.jessicacarneiro.apisrest.interfaces;

import io.github.jessicacarneiro.apisrest.domain.Driver;
import io.github.jessicacarneiro.apisrest.infrastructure.DriverRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
        expectedDriversList.add(createDriver());
        when(driverRepository.findAll()).thenReturn(expectedDriversList);

        List<Driver> actualDriversList = driverAPI.listDrivers();

        assertThat(expectedDriversList).isEqualTo(actualDriversList);
        verify(driverRepository).findAll();
    }

    private Driver createDriver() {
        long timestamp = 623866385000L;
        Driver driver = new Driver();

        driver.setId(1L);
        driver.setName("João Costa");
        driver.setDateOfBirth(new Date(timestamp));

        return driver;
    }
}