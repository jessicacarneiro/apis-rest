package io.github.jessicacarneiro.apisrest.services;

import io.github.jessicacarneiro.apisrest.domain.TravelRequest;
import io.github.jessicacarneiro.apisrest.infrastructure.TravelRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TravelService {

    @Autowired
    private TravelRequestRepository repository;

    public void saveTravelRequest(TravelRequest travelRequest) {
        repository.save(travelRequest);
    }
}
