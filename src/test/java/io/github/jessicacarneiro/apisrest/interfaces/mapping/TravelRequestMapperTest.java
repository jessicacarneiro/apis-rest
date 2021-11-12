package io.github.jessicacarneiro.apisrest.interfaces.mapping;

import io.github.jessicacarneiro.apisrest.domain.Passenger;
import io.github.jessicacarneiro.apisrest.domain.TravelRequest;
import io.github.jessicacarneiro.apisrest.domain.TravelRequestStatus;
import io.github.jessicacarneiro.apisrest.infrastructure.PassengerRepository;
import io.github.jessicacarneiro.apisrest.interfaces.PassengerAPI;
import io.github.jessicacarneiro.apisrest.interfaces.input.TravelRequestInput;
import io.github.jessicacarneiro.apisrest.interfaces.output.TravelRequestOutput;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

        Passenger passenger = generatePassenger(expectedPassengerId, expectedPassengerName);

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

    @Test
    void shouldMapOutputCorrectly() {
        Passenger passenger = generatePassenger(3L, "Marcelo Barbosa");

        TravelRequest request = generateTravelRequest(passenger, 5L, "Rua Padre Eustáquio, 23", "Avenida Franciso Sales, 48");

        TravelRequestOutput output = mapper.map(request);

        assertThat(output.getId()).isEqualTo(request.getId());
        assertThat(output.getOrigin()).isEqualTo(request.getOrigin());
        assertThat(output.getDestination()).isEqualTo(request.getDestination());
        assertThat(output.getStatus()).isEqualTo(request.getStatus());
    }

    @Test
    void shouldCreateEntityModelForOutput() {
        Passenger passenger = generatePassenger(3L, "Marcelo Barbosa");

        TravelRequest request = generateTravelRequest(passenger, 4L, "Avenida Presidente Antonio Carlos, 245", "Rua do Amendoim, 25");

        TravelRequestOutput output = new TravelRequestOutput();
        output.setId(request.getId());
        output.setOrigin(request.getOrigin());
        output.setDestination(request.getDestination());
        output.setStatus(request.getStatus());
        output.setCreationDate(request.getCreationDate());

        Link expectedLink = WebMvcLinkBuilder.linkTo(PassengerAPI.class)
                .withRel("passenger")
                .withTitle(request.getPassenger().getName());

        EntityModel<TravelRequestOutput> model = mapper.buildOutputModel(request, output);

        assertThat(model.getContent().getId()).isEqualTo(output.getId());
        assertThat(model.getContent().getCreationDate()).isEqualTo(output.getCreationDate());
        assertThat(model.getContent().getDestination()).isEqualTo(output.getDestination());
        assertThat(model.getContent().getOrigin()).isEqualTo(output.getOrigin());
        assertThat(model.getContent().getStatus()).isEqualTo(output.getStatus());
        assertThat(model.getLinks().getLink("passenger").get()).isEqualTo(expectedLink);
    }

    private Passenger generatePassenger(long id, String name) {
        Passenger passenger = new Passenger();
        passenger.setId(id);
        passenger.setName(name);
        return passenger;
    }

    private TravelRequest generateTravelRequest(Passenger passenger, long id, String origin, String destination) {
        TravelRequest request = new TravelRequest();
        request.setId(id);
        request.setPassenger(passenger);
        request.setOrigin(origin);
        request.setDestination(destination);
        request.setCreationDate(OffsetDateTime.now());
        request.setStatus(TravelRequestStatus.CREATED);
        return request;
    }
}
