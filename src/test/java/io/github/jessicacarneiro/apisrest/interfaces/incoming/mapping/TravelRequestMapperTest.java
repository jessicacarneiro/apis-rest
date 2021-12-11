package io.github.jessicacarneiro.apisrest.interfaces.incoming.mapping;

import io.github.jessicacarneiro.apisrest.domain.Passenger;
import io.github.jessicacarneiro.apisrest.domain.PassengerRepository;
import io.github.jessicacarneiro.apisrest.domain.TravelRequest;
import io.github.jessicacarneiro.apisrest.domain.TravelRequestStatus;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.input.TravelRequestInput;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.output.TravelRequestOutput;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
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

        TravelRequestOutput output = generateTravelRequestOutput(request);

        EntityModel<TravelRequestOutput> model = mapper.buildOutputModel(request, output);
        Optional<Link> link = model.getLink(LinkRelation.of("passenger"));

        assertThat(model.getContent().getId()).isEqualTo(output.getId());
        assertThat(model.getContent().getCreationDate()).isEqualTo(output.getCreationDate());
        assertThat(model.getContent().getDestination()).isEqualTo(output.getDestination());
        assertThat(model.getContent().getOrigin()).isEqualTo(output.getOrigin());
        assertThat(model.getContent().getStatus()).isEqualTo(output.getStatus());
        assertThat(link.get().getHref()).isEqualTo("/passengers/" + passenger.getId());
        assertThat(link.get().getTitle()).isEqualTo(passenger.getName());
    }

    @Test
    void shouldCreateEntityModelForListOfOutput() {
        Passenger passenger1 = generatePassenger(3L, "Marcelo Barbosa");
        Passenger passenger2 = generatePassenger(12L, "Daniel Rocha");

        TravelRequest request1 = generateTravelRequest(passenger1, 4L, "Avenida Presidente Antonio Carlos, 245", "Rua do Amendoim, 25");
        TravelRequest request2 = generateTravelRequest(passenger2, 5L,  "Rua do Amendoim, 25", "Avenida Presidente Antonio Carlos, 245");

        List<EntityModel<TravelRequestOutput>> models = mapper.buildOutputModel(Arrays.asList(request1, request2));
        assertThat(models.size()).isEqualTo(2);

        assertThat(models.get(0).getContent().getId()).isEqualTo(request1.getId());
        assertThat(models.get(0).getContent().getCreationDate()).isEqualTo(request1.getCreationDate());
        assertThat(models.get(0).getContent().getDestination()).isEqualTo(request1.getDestination());
        assertThat(models.get(0).getContent().getOrigin()).isEqualTo(request1.getOrigin());
        assertThat(models.get(0).getContent().getStatus()).isEqualTo(request1.getStatus());

        Optional<Link> link1 = models.get(0).getLink(LinkRelation.of("passenger"));
        assertThat(link1.get().getHref()).isEqualTo("/passengers/" + passenger1.getId());
        assertThat(link1.get().getTitle()).isEqualTo(passenger1.getName());

        assertThat(models.get(1).getContent().getId()).isEqualTo(request2.getId());
        assertThat(models.get(1).getContent().getCreationDate()).isEqualTo(request2.getCreationDate());
        assertThat(models.get(1).getContent().getDestination()).isEqualTo(request2.getDestination());
        assertThat(models.get(1).getContent().getOrigin()).isEqualTo(request2.getOrigin());
        assertThat(models.get(1).getContent().getStatus()).isEqualTo(request2.getStatus());

        Optional<Link> link2 = models.get(1).getLink(LinkRelation.of("passenger"));
        assertThat(link2.get().getHref()).isEqualTo("/passengers/" + passenger2.getId());
        assertThat(link2.get().getTitle()).isEqualTo(passenger2.getName());
    }

    private TravelRequestOutput generateTravelRequestOutput(TravelRequest request) {
        TravelRequestOutput output = new TravelRequestOutput();

        output.setId(request.getId());
        output.setOrigin(request.getOrigin());
        output.setDestination(request.getDestination());
        output.setStatus(request.getStatus());
        output.setCreationDate(request.getCreationDate());

        return output;
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
