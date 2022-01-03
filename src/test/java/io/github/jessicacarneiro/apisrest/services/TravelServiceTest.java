package io.github.jessicacarneiro.apisrest.services;

import io.github.jessicacarneiro.apisrest.domain.TravelRequest;
import io.github.jessicacarneiro.apisrest.domain.TravelRequestRepository;
import io.github.jessicacarneiro.apisrest.domain.TravelRequestStatus;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.AddressService;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.RouteService;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.Position;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelServiceTest {
    @Mock
    private RouteService routeService;

    @Mock
    private AddressService addressService;

    @Mock
    private TravelRequestRepository repository;

    @InjectMocks
    private TravelService travelService;

    @Test
    void shouldCreateTravelRequest() {
        TravelRequest expectedRequest = createTravelRequest(1L,null, null);
        when(repository.save(expectedRequest)).thenReturn(expectedRequest);

        TravelRequest actualRequest = travelService.saveTravelRequest(expectedRequest);

        assertThat(actualRequest.getId()).isEqualTo(expectedRequest.getId());
        assertThat(actualRequest.getStatus()).isEqualTo(expectedRequest.getStatus());
        verify(repository).save(expectedRequest);
    }

    @Test
    void shouldReturnNearbyRequests() {
        String currentDriverAddress = "Rua Tal, 34";
        String currentRequestAddress = "Rua Outra, 105";
        TravelRequest firstNearbyRequest = createTravelRequest(1L, TravelRequestStatus.CREATED, currentRequestAddress);
        TravelRequest secondNearbyRequest = createTravelRequest(2L, TravelRequestStatus.CREATED, currentRequestAddress);
        List<TravelRequest> nearbyRequests = new ArrayList<>();
        nearbyRequests.add(firstNearbyRequest);
        nearbyRequests.add(secondNearbyRequest);
        when(repository.findByStatus(TravelRequestStatus.CREATED))
                .thenReturn(nearbyRequests);

        Position positionOrigin = new Position(12, -23);
        when(addressService.getCoordinatesFromAddress(currentDriverAddress)).thenReturn(positionOrigin);
        Position positionDestination = new Position(56, -15);
        when(addressService.getCoordinatesFromAddress(currentRequestAddress)).thenReturn(positionDestination);

        List<Integer> distancesInSeconds = new ArrayList<>();
        distancesInSeconds.add(10);
        when(routeService.getTravelTimeInSeconds(positionOrigin, positionDestination)).thenReturn(distancesInSeconds);

        List<TravelRequest> actualNearbyRequests = travelService.listNearbyTravelRequests(currentDriverAddress);

        assertThat(actualNearbyRequests.size()).isEqualTo(nearbyRequests.size());
        assertThat(actualNearbyRequests.get(0)).isEqualTo(firstNearbyRequest);
        assertThat(actualNearbyRequests.get(1)).isEqualTo(secondNearbyRequest);
    }

    private TravelRequest createTravelRequest(Long id, TravelRequestStatus status, String origin) {
        TravelRequest travelRequest = new TravelRequest();
        travelRequest.setId(id);
        travelRequest.setStatus(status);
        travelRequest.setOrigin(origin);

        return travelRequest;
    }
}