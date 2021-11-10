package io.github.jessicacarneiro.apisrest.interfaces;

import io.github.jessicacarneiro.apisrest.domain.Driver;
import io.github.jessicacarneiro.apisrest.infrastructure.DriverRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

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
        LocalDate dateOfBirth = LocalDate.of(1990, 8, 16);
        expectedDriversList.add(generateDriver(1L,"João Costa", dateOfBirth));
        when(driverRepository.findAll()).thenReturn(expectedDriversList);

        List<Driver> actualDriversList = driverAPI.listDrivers();

        assertThat(expectedDriversList).isEqualTo(actualDriversList);
        verify(driverRepository).findAll();
    }

    @Test
    void shouldFindDriverById() {
        LocalDate dateOfBirth = LocalDate.of(1990, 8, 16);
        Driver expectedDriver = generateDriver(2L,"Maria Almeida", dateOfBirth);
        Long driverIdToSearch = expectedDriver.getId();
        when(driverRepository.findById(driverIdToSearch)).thenReturn(java.util.Optional.of(expectedDriver));

        Driver actualDriver = driverAPI.findDriver(driverIdToSearch);

        assertThat(actualDriver).isEqualTo(expectedDriver);
        verify(driverRepository).findById(driverIdToSearch);
    }

    @Test
    void shouldReturnIdIfSearchedDriverDoesNotExist() {
        long driverIdToSearch = 3L;
        when(driverRepository.findById(driverIdToSearch)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> driverAPI.findDriver(driverIdToSearch)).isInstanceOf(ResponseStatusException.class);
        verify(driverRepository).findById(driverIdToSearch);
    }

    @Test
    void shouldCreateANewDriver() {
        LocalDate dateOfBirth = LocalDate.of(1969, 5, 10);
        Driver expectedDriver = generateDriver(3L,"Marcos Rocha", dateOfBirth);
        when(driverRepository.save(expectedDriver)).thenReturn(expectedDriver);

        Driver createdDriver = driverAPI.createDriver(expectedDriver);

        assertThat(createdDriver).isEqualTo(expectedDriver);
        verify(driverRepository).save(expectedDriver);
    }

    @Test
    void shouldFullyUpdateADriver() {
        LocalDate dateOfBirth = LocalDate.of(1969, 5, 10);
        Driver driverToSave = generateDriver(3L,"Marcos Rocha", dateOfBirth);
        when(driverRepository.findById(driverToSave.getId())).thenReturn(java.util.Optional.of(driverToSave));
        driverToSave.setName("Alexandre Souza");
        when(driverRepository.save(driverToSave)).thenReturn(driverToSave);

        Driver fullyUpdateDriver = driverAPI.fullyUpdateDriver(3L, driverToSave);

        assertThat(fullyUpdateDriver).isEqualTo(driverToSave);
        verify(driverRepository).findById(driverToSave.getId());
        verify(driverRepository).save(driverToSave);
    }

    @Test
    void shouldPartiallyUpdateADriver() {
        LocalDate dateOfBirth = LocalDate.of(1978, 11, 28);
        Driver driverToSave = generateDriver(3L,"Helena Fonseca", dateOfBirth);
        when(driverRepository.findById(driverToSave.getId())).thenReturn(java.util.Optional.of(driverToSave));

        driverToSave.setName("Rosana Martins");
        when(driverRepository.save(driverToSave)).thenReturn(driverToSave);

        Driver fullyUpdateDriver = driverAPI.partiallyUpdateDriver(3L, driverToSave);

        assertThat(fullyUpdateDriver).isEqualTo(driverToSave);
        verify(driverRepository).findById(driverToSave.getId());
        verify(driverRepository).save(driverToSave);
    }

    @Test
    void shouldDeleteDriver() {
        LocalDate dateOfBirth = LocalDate.of(2010, 6, 10);
        Driver driverSaved = generateDriver(3L,"Joaquim Abílio", dateOfBirth);
        when(driverRepository.findById(driverSaved.getId())).thenReturn(java.util.Optional.of(driverSaved));

        driverAPI.deleteDriver(driverSaved.getId());

        verify(driverRepository).findById(driverSaved.getId());
        verify(driverRepository).delete(driverSaved);
    }

    private Driver generateDriver(long id, String name, LocalDate dateOfBirth) {
        Driver driver = new Driver();

        driver.setId(id);
        driver.setName(name);
        driver.setDateOfBirth(dateOfBirth);

        return driver;
    }
}