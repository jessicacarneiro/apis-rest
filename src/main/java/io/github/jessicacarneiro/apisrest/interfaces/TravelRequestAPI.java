package io.github.jessicacarneiro.apisrest.interfaces;

import io.github.jessicacarneiro.apisrest.interfaces.input.TravelRequestInput;
import io.github.jessicacarneiro.apisrest.interfaces.mapping.TravelRequestMapper;
import io.github.jessicacarneiro.apisrest.services.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
@RequestMapping(path = "/travelRequests", produces = MediaType.APPLICATION_JSON_VALUE)
public class TravelRequestAPI {

    @Autowired
    private TravelService travelService;

    @Autowired
    private TravelRequestMapper mapper;

    @PostMapping
    public void createTravelRequest(@RequestBody TravelRequestInput input) {
        travelService.saveTravelRequest(mapper.map(input));
    }
}
