package io.github.jessicacarneiro.apisrest.interfaces.incoming;

import io.github.jessicacarneiro.apisrest.interfaces.incoming.input.RouteRequest;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.AddressService;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.RouteService;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.Position;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
@RequestMapping(path = "/maps", produces = MediaType.APPLICATION_JSON_VALUE)
public class MapsAPI {

    @Autowired
    private AddressService addressService;

    @Autowired
    private RouteService routeService;

    @GetMapping("/address")
    public Position getAddress(@RequestParam("query") String address) {
        return addressService.getCoordinatesFromAddress(address);
    }

    @PostMapping("/route")
    public List<Integer> getRoute(@RequestBody RouteRequest request) {
        return routeService.getTravelTimeInSeconds(request.getOrigin(), request.getDestination());
    }
}
