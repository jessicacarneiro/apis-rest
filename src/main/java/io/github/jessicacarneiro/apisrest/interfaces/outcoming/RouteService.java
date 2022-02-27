package io.github.jessicacarneiro.apisrest.interfaces.outcoming;

import com.jayway.jsonpath.JsonPath;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.Position;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class RouteService {

    @Autowired
    private RestTemplate restTemplate;

    private final HttpHeaders headers = new HttpHeaders();

    @Value("${azure.maps.base.uri}")
    private String baseUri;
    @Value("${azure.maps.api.key}")
    private String apiKey;

    public RouteService() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    public List<Integer> getTravelTimeInSeconds(Position origin, Position destination) {
        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUri + "/route/directions/json")
                .queryParam("api-version", "1.0")
                .queryParam("subscription-key", apiKey)
                .queryParam("query", String.format(Locale.US, "%f,%f:%f,%f",
                        origin.getLat(), origin.getLon(), destination.getLat(), destination.getLon()));

        // TODO: add logs
        // TODO: handle specific exceptions
        try {
            String jsonResult = restTemplate.getForObject(
                    builder.build(false).toUriString(),
                    String.class,
                    origin,
                    destination);

            JSONArray rawResults = JsonPath.parse(jsonResult).read("$..routes[0].summary.travelTimeInSeconds");

            return rawResults.stream().map(it -> ((Integer) it)).collect(Collectors.toList());
        } catch (Exception exception) {
            return null;
        }
    }
}
