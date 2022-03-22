package io.github.jessicacarneiro.apisrest.interfaces.incoming;

import io.github.jessicacarneiro.apisrest.domain.Driver;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.errorhandling.ErrorResponse;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.output.Drivers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Driver API", description = "Handle drivers data.")
public interface DriverAPI {

    @Operation(description = "List all drivers")
    Drivers listDrivers(
        @Parameter(description = "Page number for pagination purposes")
        @RequestParam(name = "page", defaultValue = "0") int page
    );

    @Operation(
            description = "Search for a specific driver",
            responses = {
                    @ApiResponse(responseCode = "200", description = "If the driver is found"),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If the driver is not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    Driver findDriver(@Parameter(description = "Driver's ID to be find")
                      @PathVariable("id") Long id);


    @Operation(
            description = "Add a new driver to the platform",
            responses = {
                    @ApiResponse(responseCode = "200", description = "If the driver was correctly created")
            }
    )
    Driver createDriver(@RequestBody Driver driver);

    @Operation(
            description = "Partially update a driver",
            responses = {
                    @ApiResponse(responseCode = "200", description = "If the driver was correctly updated"),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If the driver is not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    Driver partiallyUpdateDriver(
            @Parameter(description = "Driver's ID to be updated")
            @PathVariable("id") long id,
            @RequestBody Driver driver
    );

    @Operation(
            description = "Fully update a driver",
            responses = {
                    @ApiResponse(responseCode = "200", description = "If the driver was correctly updated"),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If the driver is not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    Driver fullyUpdateDriver(
            @Parameter(description = "Driver's ID to be updated")
            @PathVariable("id") Long id,
            @RequestBody Driver driver
    );

    @Operation(
            description = "Delete a driver",
            responses = {
                    @ApiResponse(responseCode = "200", description = "If the driver was correctly deleted"),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "If the driver is not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    void deleteDriver(
            @Parameter(description = "Driver's ID to be updated")
            @PathVariable("id") Long id
    );
}
