package io.github.jessicacarneiro.apisrest.interfaces.outcoming;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.Position;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.Route;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.RouteResponse;
import io.github.jessicacarneiro.apisrest.interfaces.outcoming.output.Summary;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class RouteServiceTest {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RouteService service;

    @Value("${azure.maps.route.base.uri}")
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

        Position origin = generatePosition(new BigDecimal(-12.4452), new BigDecimal(24.69340));
        Position destination = generatePosition(new BigDecimal(-12.3252), new BigDecimal(25.1402));

        String url = String.
                format(Locale.US, "%s?api-version=1.0&subscription-key=%s&query=%f,%f:%f,%f", baseUrl, apiKey,
                        origin.getLat(), origin.getLon(), destination.getLat(), destination.getLon());

        int expectedDistanceInMeters = 12458;
        RouteResponse response = generateRouteResponse(expectedDistanceInMeters);
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

        int actualDistanceInMeters = service.getDistanceInMeters(origin, destination);

        assertThat(actualDistanceInMeters).isEqualTo(expectedDistanceInMeters);
        server.verify();
    }

    @Test
    void shouldReturnNullIfNoResponseIsReturned() throws JsonProcessingException, UnsupportedEncodingException {
        MockRestServiceServer server = setUp();

        Position origin = generatePosition(new BigDecimal(38.4452), new BigDecimal(-18.69340));
        Position destination = generatePosition(new BigDecimal(39.3252), new BigDecimal(-19.1402));

        String url = String.
                format(Locale.US, "%s?api-version=1.0&subscription-key=%s&query=%f,%f:%f,%f", baseUrl, apiKey,
                        origin.getLat(), origin.getLon(), destination.getLat(), destination.getLon());

        server.expect(
                ExpectedCount.once(),
                MockRestRequestMatchers.requestTo(url)
        ).andRespond(
                MockRestResponseCreators.withBadRequest()
        );

        Integer actualDistanceInMeters = service.getDistanceInMeters(origin, destination);

        assertThat(actualDistanceInMeters).isNull();
        server.verify();
    }

    private Position generatePosition(BigDecimal latitude, BigDecimal longitude) {
        Position position = new Position();
        position.setLat(latitude);
        position.setLon(longitude);

        return position;
    }

    private RouteResponse generateRouteResponse(int distance) {
        Summary summary = new Summary();
        summary.setLengthInMeters(distance);

        Route route = new Route();
        route.setSummary(summary);

        List<Route> routes = new ArrayList<>();
        routes.add(route);

        RouteResponse response = new RouteResponse();

        response.setRoutes(routes);

        return response;
    }
}