package io.github.jessicacarneiro.apisrest.interfaces.outcoming;

import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.Position;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.RouteResponse;
import java.util.Collections;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class RouteService {

    @Autowired
    private RestTemplate restTemplate;

    private final HttpHeaders headers = new HttpHeaders();

    @Value("${azure.maps.route.base.uri}")
    private String baseUri;
    @Value("${azure.maps.api.key}")
    private String apiKey;

    public RouteService() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    public Integer getDistanceFromPlaces(Position origin, Position destination) {
        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUri)
                .queryParam("api-version", "1.0")
                .queryParam("subscription-key", apiKey)
                .queryParam("query", String.format(Locale.US, "%f,%f:%f,%f",
                        origin.getLat(), origin.getLon(), destination.getLat(), destination.getLon()));

        // TODO: add logs
        // TODO: handle specific exceptions
        try {
            HttpEntity<RouteResponse> response = restTemplate.exchange(
                    builder.build(false).toUriString(),
                    HttpMethod.GET,
                    entity,
                    RouteResponse.class);

            return response.getBody().getRoutes().get(0).getSummary().getLengthInMeters();
        } catch (Exception exception) {
            return null;
        }
    }
}
