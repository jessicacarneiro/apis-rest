package io.github.jessicacarneiro.apisrest.services;

import io.github.jessicacarneiro.apisrest.domain.TravelRequest;
import io.github.jessicacarneiro.apisrest.infrastructure.TravelRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TravelServiceTest {
    @Mock
    private TravelRequestRepository travelRequestRepository;

    @InjectMocks
    private TravelService travelService;

    @Test
    void shouldCreateTravelRequest() {
        TravelRequest request = new TravelRequest();

        travelService.saveTravelRequest(request);

        verify(travelRequestRepository).save(request);
    }
}