package io.github.jessicacarneiro.apisrest.services;

import io.github.jessicacarneiro.apisrest.domain.TravelRequest;
import io.github.jessicacarneiro.apisrest.infrastructure.TravelRequestRepository;
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
    private TravelRequestRepository repository;

    @InjectMocks
    private TravelService travelService;

    @Test
    void shouldCreateTravelRequest() {
        TravelRequest expectedRequest = new TravelRequest();
        when(repository.save(expectedRequest)).thenReturn(expectedRequest);

        TravelRequest actualRequest = travelService.saveTravelRequest(expectedRequest);

        assertThat(actualRequest).isEqualTo(expectedRequest);
        verify(repository).save(expectedRequest);
    }
}