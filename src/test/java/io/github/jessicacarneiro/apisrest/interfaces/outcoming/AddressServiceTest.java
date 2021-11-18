package io.github.jessicacarneiro.apisrest.interfaces.outcoming;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.AddressResponse;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.Position;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.Result;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class AddressServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AddressService service;

    @Value("${azure.maps.address.base.uri}")
    private String baseUrl;

    @Value("${azure.maps.api.key}")
    private String apiKey;

    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    private MockRestServiceServer setUp() {
        return MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldReturnCoordinatesIfAddressIsFound() throws JsonProcessingException, UnsupportedEncodingException {
        MockRestServiceServer server = setUp();

        String address = "Rua das Pedras 250";
        String url = String.
                format("%s?api-version=1.0&subscription-key=%s&query=%s", baseUrl, apiKey,
                        URLEncoder.encode(address, "UTF-8").replace("+", "%20"));

        double latitude = 35.58616;
        double longitude = -18.5893;

        AddressResponse response = generateAddressResponse(latitude, longitude);
        String responseBody = objectWriter.writeValueAsString(response);

        server.expect(
                ExpectedCount.once(),
                MockRestRequestMatchers.requestTo(url)
        ).andRespond(
                MockRestResponseCreators.withSuccess(
                        responseBody,
                        MediaType.APPLICATION_JSON
                )
        );

        Position positionReturned = service.getCoordinatesFromAddress(address);

        assertThat(positionReturned.getLat()).isEqualTo(latitude);
        assertThat(positionReturned.getLon()).isEqualTo(longitude);

        server.verify();
    }

    @Test
    void shouldReturnNullIfNoResponseIsReturned() throws JsonProcessingException, UnsupportedEncodingException {
        MockRestServiceServer server = setUp();

        String address = "Rua do Morro 2588";
        String url = String.
                format("%s?api-version=1.0&subscription-key=%s&query=%s", baseUrl, apiKey,
                        URLEncoder.encode(address, "UTF-8").replace("+", "%20"));

        server.expect(
                ExpectedCount.once(),
                MockRestRequestMatchers.requestTo(url)
        ).andRespond(
                MockRestResponseCreators.withBadRequest()
        );

        Position positionReturned = service.getCoordinatesFromAddress(address);

        assertThat(positionReturned).isNull();

        server.verify();
    }

    private AddressResponse generateAddressResponse(double latitude, double longitude) {
        Position position = new Position();
        position.setLat(latitude);
        position.setLon(longitude);

        Result result = new Result();
        result.setPosition(position);

        List<Result> results = new ArrayList<>();
        results.add(result);

        AddressResponse response = new AddressResponse();
        response.setResults(results);

        return response;
    }
}
