package io.github.jessicacarneiro.apisrest.wiremockserver;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import io.github.jessicacarneiro.apisrest.FileHandler;
import io.github.jessicacarneiro.apisrest.SpringSecurityWebTestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = SpringSecurityWebTestConfig.class
)
@ContextConfiguration
@ActiveProfiles("test")
public class TravelRequestAPITestIT {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private static WireMockServer server;

    @Test
    @WithUserDetails("manager@company.com")
    public void testFindNearbyTravelRequests() {
        setUpServer();

        String passengerId = given()
                .contentType(ContentType.JSON)
                .body(FileHandler.loadFileContents("/requests/passengers-apis/create-new-passenger.json"))
                .post("/passengers")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", is("Jéssica Carneiro"))
                .extract()
                .body()
                .jsonPath().getString("id");

        Map<String, String> data = new HashMap<>();
        data.put("passengerId", passengerId);

        Integer travelRequestId = given()
                .contentType(ContentType.JSON)
                .body(FileHandler.loadFileContents("/requests/travel-requests-api/create-new-request.json", data))
                .post("/travelRequests")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("destination", is("central park"))
                .body("origin", is("empire state building"))
                .body("status", is("CREATED"))
                .body("_links.passenger.title", is("Jéssica Carneiro"))
                .extract()
                .jsonPath()
                .get("id");

        given()
                .get("/travelRequests/nearby?currentAddress=central park")
                .then()
                .statusCode(200)
                .body("[0].id", is(travelRequestId))
                .body("[0].destination", is("central park"))
                .body("[0].origin", is("empire state building"))
                .body("[0].status", is("CREATED"));
    }

    @BeforeEach
    void beforeAll() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        server = new WireMockServer(options().port(8082));
        server.start();
    }

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssured.port = port;
        RestAssured.useRelaxedHTTPSValidation();
    }

    public void setUpServer() {
        Map<String, StringValuePattern> queryParams = new HashMap<>();
        queryParams.put("api-version", equalTo("1.0"));
        queryParams.put("subscription-key", equalTo("blablabla"));
        queryParams.put("query", equalTo("central park"));


        server.stubFor(
                get(urlPathEqualTo(
                        "/search/address/json"
                ))
                        .withQueryParams(queryParams)
                        .willReturn(okJson(FileHandler.loadFileContents("/responses/azure-maps/address-central-park.json")))
        );

        queryParams.remove("query");
        queryParams.put("query", equalTo("empire state building"));

        server.stubFor(
                get(urlPathEqualTo(
                                "/search/address/json"
                        ))
                        .withQueryParams(queryParams)
                        .willReturn(okJson(FileHandler.loadFileContents("/responses/azure-maps/address-empire-state-building.json")))
        );

        queryParams.remove("query");
        queryParams.put("query", equalTo("40.999410,-73.815650:42.575870,-73.683650"));

        server.stubFor(
                get(urlPathEqualTo(
                        "/route/directions/json"
                ))
                        .withQueryParams(queryParams)
                        .willReturn(okJson(FileHandler.loadFileContents("/responses/azure-maps/route.json")))
        );
    }
}
