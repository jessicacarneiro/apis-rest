package io.github.jessicacarneiro.apisrest.interfaces.outcoming;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.Position;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
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

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class RouteServiceTest {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RouteService service;

    @Value("${azure.maps.base.uri}")
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

        Position origin = generatePosition(-12.4452, 24.69340);
        Position destination = generatePosition(-12.3252,25.1402);

        String url = String.
                format(Locale.US, "%s/route/directions/json?api-version=1.0&subscription-key=%s&query=%f,%f:%f,%f", baseUrl, apiKey,
                        origin.getLat(), origin.getLon(), destination.getLat(), destination.getLon());

        int expectedTravelTime = 12458;
        String response = generateRouteResponse(expectedTravelTime);

        server.expect(
                ExpectedCount.once(),
                MockRestRequestMatchers.requestTo(url)
        ).andRespond(
                MockRestResponseCreators.withSuccess(
                        response,
                        MediaType.APPLICATION_JSON
                )
        );

        List<Integer> actualTravelTime = service.getTravelTimeInSeconds(origin, destination);

        assertThat(actualTravelTime.get(0)).isEqualTo(expectedTravelTime);
        server.verify();
    }

    @Test
    void shouldReturnEmptyListIfNoResponseIsReturned() throws JsonProcessingException, UnsupportedEncodingException {
        MockRestServiceServer server = setUp();

        Position origin = generatePosition(38.4452, -18.69340);
        Position destination = generatePosition(39.3252, -19.1402);

        String url = String.
                format(Locale.US, "%s/route/directions/json?api-version=1.0&subscription-key=%s&query=%f,%f:%f,%f", baseUrl, apiKey,
                        origin.getLat(), origin.getLon(), destination.getLat(), destination.getLon());

        server.expect(
                ExpectedCount.once(),
                MockRestRequestMatchers.requestTo(url)
        ).andRespond(
                MockRestResponseCreators.withBadRequest()
        );

        List<Integer> actualTravelTime = service.getTravelTimeInSeconds(origin, destination);

        assertThat(actualTravelTime.isEmpty()).isTrue();
        server.verify();
    }

    private Position generatePosition(double latitude, double longitude) {
        Position position = new Position();
        position.setLat(latitude);
        position.setLon(longitude);

        return position;
    }

    private String generateRouteResponse(int expectedTravelTime) {
        return "{\n" +
                "    \"routes\": [\n" +
                "        {\n" +
                "            \"summary\": {\n" +
                "                \"travelTimeInSeconds\": " + expectedTravelTime + "\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}