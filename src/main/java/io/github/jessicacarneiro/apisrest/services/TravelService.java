package io.github.jessicacarneiro.apisrest.services;

import io.github.jessicacarneiro.apisrest.domain.TravelRequest;
import io.github.jessicacarneiro.apisrest.domain.TravelRequestRepository;
import io.github.jessicacarneiro.apisrest.domain.TravelRequestStatus;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.AddressService;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.RouteService;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.Position;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import static io.github.jessicacarneiro.apisrest.domain.TravelRequestStatus.CREATED;

@Component
public class TravelService {

    private final TravelRequestRepository repository;
    private final AddressService addressService;
    private final RouteService routeService;

    private static final int MAX_TRAVEL_TIME = 12000;

    public TravelService(TravelRequestRepository repository, AddressService addressService, RouteService routeService) {
        this.repository = repository;
        this.addressService = addressService;
        this.routeService = routeService;
    }

    public TravelRequest saveTravelRequest(TravelRequest travelRequest) {
        travelRequest.setStatus(CREATED);
        travelRequest.setCreationDate(OffsetDateTime.now());

        return repository.save(travelRequest);
    }

    public List<TravelRequest> listNearbyTravelRequests(String currentAddress) {
        List<TravelRequest> requests = repository.findByStatus(TravelRequestStatus.CREATED);

        List<TravelRequest> nearbyRequests = requests
                .stream()
                .filter(tr -> isNearby(currentAddress, tr.getOrigin()))
                .collect(Collectors.toList());

        return nearbyRequests;
    }

    private Boolean isNearby(String origin, String destination) {
        Position positionOrigin = addressService.getCoordinatesFromAddress(origin);
        Position positionDestination = addressService.getCoordinatesFromAddress(destination);

        Boolean isNearby = routeService.getTravelTimeInSeconds(positionOrigin, positionDestination).get(0) <= MAX_TRAVEL_TIME;

        return isNearby;
    }
}
