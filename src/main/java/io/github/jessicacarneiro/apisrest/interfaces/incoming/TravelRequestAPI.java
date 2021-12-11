package io.github.jessicacarneiro.apisrest.interfaces.incoming;

import io.github.jessicacarneiro.apisrest.domain.TravelRequest;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.input.TravelRequestInput;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.mapping.TravelRequestMapper;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.output.TravelRequestOutput;
import io.github.jessicacarneiro.apisrest.services.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@RestController
@RequestMapping(path = "/travelRequests", produces = MediaType.APPLICATION_JSON_VALUE)
public class TravelRequestAPI {

    @Autowired
    private TravelService travelService;

    @Autowired
    private TravelRequestMapper mapper;

    @PostMapping
    public EntityModel<TravelRequestOutput> createTravelRequest(@RequestBody TravelRequestInput input) {
        TravelRequest travelRequest = travelService.saveTravelRequest(mapper.map(input));
        TravelRequestOutput output = mapper.map(travelRequest);

        return mapper.buildOutputModel(travelRequest, output);
    }

    @GetMapping("/nearby")
    public List<EntityModel<TravelRequestOutput>> listNearbyRequests(@RequestParam String currentAddress) {
        List<TravelRequest> requests = travelService.listNearbyTravelRequests(currentAddress);

        return mapper.buildOutputModel(requests);
    }
}