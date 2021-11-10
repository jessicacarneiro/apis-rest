package io.github.jessicacarneiro.apisrest.interfaces.mapping;

import io.github.jessicacarneiro.apisrest.domain.Passenger;
import io.github.jessicacarneiro.apisrest.domain.TravelRequest;
import io.github.jessicacarneiro.apisrest.infrastructure.PassengerRepository;
import io.github.jessicacarneiro.apisrest.interfaces.input.TravelRequestInput;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelRequestMapperTest {
    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private TravelRequestMapper mapper;

    @Test
    void shouldMapInputCorrectlyIfPassengerExists() {
        Long expectedPassengerId = 3L;
        String expectedPassengerName = "Marcelo Barbosa";
        String expectedOrigin = "Avenida Amazonas, 245";
        String expectedDestination = "Rua do Amendoim, 25";

        Passenger passenger = new Passenger();
        passenger.setId(expectedPassengerId);
        passenger.setName(expectedPassengerName);

        TravelRequestInput input = new TravelRequestInput();
        input.setPassengerId(expectedPassengerId);
        input.setOrigin(expectedOrigin);
        input.setDestination(expectedDestination);

        when(passengerRepository.findById(passenger.getId())).thenReturn(java.util.Optional.of(passenger));

        TravelRequest travelRequest = mapper.map(input);

        assertThat(travelRequest.getPassenger().getId()).isEqualTo(expectedPassengerId);
        assertThat(travelRequest.getPassenger().getName()).isEqualTo(expectedPassengerName);
        assertThat(travelRequest.getOrigin()).isEqualTo(expectedOrigin);
        assertThat(travelRequest.getDestination()).isEqualTo(expectedDestination);

        verify(passengerRepository).findById(expectedPassengerId);
    }

    @Test
    void shouldThrowExceptionIfPassengerDoesNotExist() {
        Long passengerIdToSearch = 3L;

        TravelRequestInput input = new TravelRequestInput();
        input.setPassengerId(passengerIdToSearch);
        input.setOrigin("Avenida do Contorno, 123");
        input.setDestination("Rua das Flores, 456");

        when(passengerRepository.findById(passengerIdToSearch)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mapper.map(input)).isInstanceOf(ResponseStatusException.class);
        verify(passengerRepository).findById(passengerIdToSearch);
    }
}
