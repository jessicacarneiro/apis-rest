package io.github.jessicacarneiro.apisrest.interfaces.incoming.mapping;

import io.github.jessicacarneiro.apisrest.domain.Passenger;
import io.github.jessicacarneiro.apisrest.domain.PassengerRepository;
import io.github.jessicacarneiro.apisrest.domain.TravelRequest;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.PassengerAPIImpl;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.input.TravelRequestInput;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.output.TravelRequestOutput;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class TravelRequestMapper {

    @Autowired
    private PassengerRepository passengerRepository;

    public TravelRequest map(TravelRequestInput input) {
        Passenger passenger = passengerRepository.findById(input.getPassengerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        TravelRequest travelRequest = new TravelRequest();
        travelRequest.setPassenger(passenger);
        travelRequest.setOrigin(input.getOrigin());
        travelRequest.setDestination(input.getDestination());

        return travelRequest;
    }

    public TravelRequestOutput map(TravelRequest travelRequest) {
        TravelRequestOutput output = new TravelRequestOutput();

        output.setId(travelRequest.getId());
        output.setOrigin(travelRequest.getOrigin());
        output.setDestination(travelRequest.getDestination());
        output.setCreationDate(travelRequest.getCreationDate());
        output.setStatus(travelRequest.getStatus());

        return output;
    }

    public EntityModel<TravelRequestOutput> buildOutputModel(TravelRequest travelRequest, TravelRequestOutput output) {
        EntityModel<TravelRequestOutput> model = new EntityModel<>(output);

        Link passengerLink = WebMvcLinkBuilder.linkTo(PassengerAPIImpl.class)
                .slash(travelRequest.getPassenger().getId())
                .withRel("passenger")
                .withTitle(travelRequest.getPassenger().getName());

        model.add(passengerLink);

        return EntityModel.of(output).add(passengerLink);
    }

    public List<EntityModel<TravelRequestOutput>> buildOutputModel(List<TravelRequest> travelRequests) {
        return travelRequests
                .stream()
                .map(tr -> buildOutputModel(tr, map(tr)))
                .collect(Collectors.toList());
    }
}
