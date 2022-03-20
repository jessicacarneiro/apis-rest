package io.github.jessicacarneiro.apisrest.interfaces.incoming;

import io.github.jessicacarneiro.apisrest.domain.Passenger;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.errorhandling.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Passenger API", description = "Handles passengers data.")
public interface PassengerAPI {

    @Operation(description = "Get all passengers")
    List<Passenger> listPassengers();

    @Operation(
            description = "Get a specific passenger",
            responses = {
                    @ApiResponse(responseCode = "200", description = "If the passenger is found"),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If the passenger is not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    Passenger findPassenger(
            @Parameter(description = "Passenger's ID to be searched")
            @PathVariable("id") Long id
    );

    @Operation(
            description = "Create a new passenger",
            responses = {
                    @ApiResponse(responseCode = "200", description = "If the passenger was correctly created")
            }
    )
    Passenger createPassenger(@RequestBody Passenger passenger);

    @Operation(
            description = "Fully update a specific passenger",
            responses = {
                    @ApiResponse(responseCode = "200", description = "If the passenger is updated"),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If the passenger is not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    Passenger fullyUpdatePassenger(
            @Parameter(description = "Passenger's ID to be updated")
            @PathVariable("id") Long id,
            @RequestBody Passenger passenger
    );

    @Operation(
            description = "Partially update a specific passenger",
            responses = {
                    @ApiResponse(responseCode = "200", description = "If the passenger is updated"),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If the passenger is not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    Passenger partiallyUpdatePassenger(
            @Parameter(description = "Passenger's ID to be updated")
            @PathVariable("id") Long id,
            @RequestBody Passenger passenger
    );

    @Operation(
            description = "Delete a specific passenger",
            responses = {
                    @ApiResponse(responseCode = "200", description = "If the passenger is deleted"),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If the passenger is not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    void deletePassenger(
            @Parameter(description = "Passenger's ID to be deleted")
            @PathVariable("id") Long id
    );
}
