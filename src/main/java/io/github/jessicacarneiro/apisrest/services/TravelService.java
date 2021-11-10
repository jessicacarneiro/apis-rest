package io.github.jessicacarneiro.apisrest.services;

import io.github.jessicacarneiro.apisrest.domain.TravelRequest;
import io.github.jessicacarneiro.apisrest.infrastructure.TravelRequestRepository;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Component;

import static io.github.jessicacarneiro.apisrest.domain.TravelRequestStatus.CREATED;

@Component
public class TravelService {

    private final TravelRequestRepository repository;

    public TravelService(TravelRequestRepository repository) {
        this.repository = repository;
    }

    public void saveTravelRequest(TravelRequest travelRequest) {
        travelRequest.setStatus(CREATED);
        travelRequest.setCreationDate(OffsetDateTime.now());

        repository.save(travelRequest);
    }
}
