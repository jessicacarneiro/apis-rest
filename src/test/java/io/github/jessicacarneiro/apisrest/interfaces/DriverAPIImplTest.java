package io.github.jessicacarneiro.apisrest.interfaces;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.jessicacarneiro.apisrest.domain.Driver;
import io.github.jessicacarneiro.apisrest.domain.DriverRepository;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.DriverAPIImpl;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.errorhandling.exceptions.UserNotFoundException;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.output.Drivers;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class DriverAPIImplTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverAPIImpl driverAPIImpl;

    @Test
    void shouldReturnEmptyListIfNoDriversRegistered() {
        int page = 1;
        int pageSize = 10;

        when(driverRepository.findAll(PageRequest.of(page, pageSize))).thenReturn(Page.empty());

        Drivers response = driverAPIImpl.listDrivers(page);

        assertThat(response.getDrivers().isEmpty()).isTrue();
    }

    @Test
    void shouldReturnListWithRegisteredDrivers() {
        int page = 0;
        int pageSize = 10;
        List<Driver> expectedDriversList = new ArrayList<>();
        LocalDate dateOfBirth = LocalDate.of(1990, 8, 16);
        expectedDriversList.add(generateDriver(1L,"João Costa", dateOfBirth));
        Page<Driver> pagedResponse = new PageImpl<>(expectedDriversList);
        when(driverRepository.findAll(PageRequest.of(page, pageSize))).thenReturn(pagedResponse);

        Drivers actualDriversList = driverAPIImpl.listDrivers(page);

        assertThat(actualDriversList.getDrivers().size()).isEqualTo(expectedDriversList.size());

        Driver actualDriver = actualDriversList.getDrivers().get(0).getContent();
        assertThat(actualDriver.getId()).isEqualTo(expectedDriversList.get(0).getId());
        assertThat(actualDriver.getName()).isEqualTo(expectedDriversList.get(0).getName());
        assertThat(actualDriver.getDateOfBirth()).isEqualTo(expectedDriversList.get(0).getDateOfBirth());
        
        verify(driverRepository).findAll(PageRequest.of(page, pageSize));
    }

    @Test
    void shouldFindDriverById() throws UserNotFoundException {
        LocalDate dateOfBirth = LocalDate.of(1990, 8, 16);
        Driver expectedDriver = generateDriver(2L,"Maria Almeida", dateOfBirth);
        Long driverIdToSearch = expectedDriver.getId();
        when(driverRepository.findById(driverIdToSearch)).thenReturn(java.util.Optional.of(expectedDriver));

        Driver actualDriver = driverAPIImpl.findDriver(driverIdToSearch);

        assertThat(actualDriver).isEqualTo(expectedDriver);
        verify(driverRepository).findById(driverIdToSearch);
    }

    @Test
    void shouldReturnIdIfSearchedDriverDoesNotExist() {
        long driverIdToSearch = 3L;
        when(driverRepository.findById(driverIdToSearch)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> driverAPIImpl.findDriver(driverIdToSearch)).isInstanceOf(UserNotFoundException.class);
        verify(driverRepository).findById(driverIdToSearch);
    }

    @Test
    void shouldCreateANewDriver() {
        LocalDate dateOfBirth = LocalDate.of(1969, 5, 10);
        Driver expectedDriver = generateDriver(3L,"Marcos Rocha", dateOfBirth);
        when(driverRepository.save(expectedDriver)).thenReturn(expectedDriver);

        Driver createdDriver = driverAPIImpl.createDriver(expectedDriver);

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

        Driver fullyUpdateDriver = driverAPIImpl.fullyUpdateDriver(3L, driverToSave);

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

        Driver fullyUpdateDriver = driverAPIImpl.partiallyUpdateDriver(3L, driverToSave);

        assertThat(fullyUpdateDriver).isEqualTo(driverToSave);
        verify(driverRepository).findById(driverToSave.getId());
        verify(driverRepository).save(driverToSave);
    }

    @Test
    void shouldDeleteDriver() {
        LocalDate dateOfBirth = LocalDate.of(2010, 6, 10);
        Driver driverSaved = generateDriver(3L,"Joaquim Abílio", dateOfBirth);
        when(driverRepository.findById(driverSaved.getId())).thenReturn(java.util.Optional.of(driverSaved));

        driverAPIImpl.deleteDriver(driverSaved.getId());

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