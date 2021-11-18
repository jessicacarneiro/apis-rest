package io.github.jessicacarneiro.apisrest.interfaces.outcoming;

import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.AddressResponse;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.Position;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AddressService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final HttpHeaders headers = new HttpHeaders();

    @Value("${azure.maps.address.base.uri}")
    private String baseUri;
    @Value("${azure.maps.api.key}")
    private String apiKey;

    public AddressService() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    public Position getCoordinatesFromAddress(String address) {
        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUri)
                .queryParam("api-version", "1.0")
                .queryParam("subscription-key", apiKey)
                .queryParam("query", address);

        // TODO: add logs
        // TODO: handle specific exceptions
        try {
            HttpEntity<AddressResponse> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    AddressResponse.class);

            return response.getBody().getResults().get(0).getPosition();
        } catch (Exception exception) {
            return null;
        }
    }
}
