package io.github.jessicacarneiro.apisrest.interfaces.incoming;

import io.github.jessicacarneiro.apisrest.interfaces.incoming.errorhandling.ErrorResponse;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.input.TravelRequestInput;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.output.TravelRequestOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Travel Request API", description = "Handles travel requests created for passengers")
public interface TravelRequestAPI {

    @Operation(
            description = "Creates a new travel request for a specific passenger",
            responses = {
                    @ApiResponse(responseCode = "200", description = "If travel request is created"),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If passenger is not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    EntityModel<TravelRequestOutput> createTravelRequest(@RequestBody @Valid TravelRequestInput input);

    @Operation(description = "List all travel requests near a specific address")
    List<EntityModel<TravelRequestOutput>> listNearbyRequests(
            @Parameter(description = "Current address used to search travel requests near it")
            @RequestParam String currentAddress
    );
}
