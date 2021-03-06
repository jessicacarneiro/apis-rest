package io.github.jessicacarneiro.apisrest.interfaces;

import io.github.jessicacarneiro.apisrest.domain.Passenger;
import io.github.jessicacarneiro.apisrest.domain.PassengerRepository;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.PassengerAPIImpl;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.errorhandling.exceptions.UserNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PassengerAPIImplTest {

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerAPIImpl passengerAPIImpl;

    @Test
    void shouldReturnEmptyListIfNoPassengersRegistered() {
        when(passengerRepository.findAll()).thenReturn(Collections.emptyList());

        List<Passenger> actualPassengers = passengerAPIImpl.listPassengers();

        assertThat(actualPassengers).isEmpty();
        verify(passengerRepository).findAll();
    }

    @Test
    void shouldReturnPassengersListIfPassengersRegistered() {
        List<Passenger> expectedPassengers = new ArrayList<>();
        expectedPassengers.add(generatePassenger(1L, "José Campos"));
        when(passengerRepository.findAll()).thenReturn(expectedPassengers);

        List<Passenger> actualPassengers = passengerAPIImpl.listPassengers();

        assertThat(actualPassengers.size()).isEqualTo(expectedPassengers.size());
        assertThat(actualPassengers.get(0)).isEqualTo(expectedPassengers.get(0));
        verify(passengerRepository).findAll();
    }

    @Test
    void shouldReturnPassengerWhenSearchingById() {
        Passenger expectedPassenger = generatePassenger(2L, "Mario Campos");
        when(passengerRepository.findById(expectedPassenger.getId())).thenReturn(java.util.Optional.of(expectedPassenger));

        Passenger actualPassenger = passengerAPIImpl.findPassenger(expectedPassenger.getId());

        assertThat(actualPassenger.getId()).isEqualTo(expectedPassenger.getId());
        assertThat(actualPassenger.getName()).isEqualTo(expectedPassenger.getName());
        verify(passengerRepository).findById(expectedPassenger.getId());
    }

    @Test
    void shouldReturnIdIfPassengerDoesNotExist() {
        long passengerIdToSearch = 3L;
        when(passengerRepository.findById(passengerIdToSearch)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> passengerAPIImpl.findPassenger(passengerIdToSearch)).isInstanceOf(UserNotFoundException.class);
        verify(passengerRepository).findById(passengerIdToSearch);
    }

    @Test
    void shouldCreateANewPassenger() {
        Passenger expectedPassenger = generatePassenger(3L, "Matheus Aguiar");
        when(passengerRepository.save(expectedPassenger)).thenReturn(expectedPassenger);

        Passenger actualPassenger = passengerAPIImpl.createPassenger(expectedPassenger);

        assertThat(actualPassenger.getId()).isEqualTo(expectedPassenger.getId());
        assertThat(actualPassenger.getName()).isEqualTo(expectedPassenger.getName());
        verify(passengerRepository).save(expectedPassenger);
    }

    @Test
    void shouldFullyUpdatePassenger() {
        Passenger savedPassenger = generatePassenger(4L, "Iara Lopes");
        when(passengerRepository.findById(savedPassenger.getId())).thenReturn(java.util.Optional.of(savedPassenger));
        Passenger expectedPassenger = generatePassenger(4L, "Carol Lopes");
        when(passengerRepository.save(expectedPassenger)).thenReturn(expectedPassenger);

        Passenger actualPassenger = passengerAPIImpl.fullyUpdatePassenger(savedPassenger.getId(), expectedPassenger);

        assertThat(actualPassenger.getId()).isEqualTo(savedPassenger.getId());
        assertThat(actualPassenger.getName()).isEqualTo(savedPassenger.getName());
        verify(passengerRepository).findById(savedPassenger.getId());
        verify(passengerRepository).save(expectedPassenger);
    }

    @Test
    void shouldPartiallyUpdatePassenger() {
        Passenger savedPassenger = generatePassenger(4L, "Iara Lopes");
        when(passengerRepository.findById(savedPassenger.getId())).thenReturn(java.util.Optional.of(savedPassenger));
        Passenger expectedPassenger = generatePassenger(4L, "Carol Lopes");
        when(passengerRepository.save(expectedPassenger)).thenReturn(expectedPassenger);

        Passenger actualPassenger = passengerAPIImpl.partiallyUpdatePassenger(savedPassenger.getId(), expectedPassenger);

        assertThat(actualPassenger.getId()).isEqualTo(savedPassenger.getId());
        assertThat(actualPassenger.getName()).isEqualTo(savedPassenger.getName());
        verify(passengerRepository).findById(savedPassenger.getId());
        verify(passengerRepository).save(expectedPassenger);
    }

    @Test
    void shouldDeletePassenger() {
        Passenger passengerSaved = generatePassenger(3L, "Márcia Abílio");
        when(passengerRepository.findById(passengerSaved.getId())).thenReturn(java.util.Optional.of(passengerSaved));

        passengerAPIImpl.deletePassenger(passengerSaved.getId());

        verify(passengerRepository).findById(passengerSaved.getId());
        verify(passengerRepository).delete(passengerSaved);
    }

    private Passenger generatePassenger(Long id, String name) {
        Passenger passenger = new Passenger();

        passenger.setId(id);
        passenger.setName(name);

        return passenger;
    }
}