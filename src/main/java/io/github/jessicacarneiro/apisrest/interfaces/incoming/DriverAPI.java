package io.github.jessicacarneiro.apisrest.interfaces.incoming;

import io.github.jessicacarneiro.apisrest.domain.Driver;
import io.github.jessicacarneiro.apisrest.domain.DriverRepository;
import io.github.jessicacarneiro.apisrest.interfaces.incoming.errorhandling.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Service
@RestController
@RequestMapping(path = "/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Driver API", description = "Handle drivers data.")
public class DriverAPI {

    @Autowired
    private DriverRepository repository;

    @GetMapping
    @Operation(description = "Get all drivers")
    public List<Driver> listDrivers() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
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
    public Driver findDriver(@Parameter(description = "Driver's ID to be find")
                             @PathVariable("id") Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(
            description = "Add a new driver to the platform",
            responses = {
                    @ApiResponse(responseCode = "200", description = "If the driver was correctly created")
            }
    )
    public Driver createDriver(@RequestBody Driver driver) {
        return repository.save(driver);
    }

    @PatchMapping("/{id}")
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
    public Driver partiallyUpdateDriver(
            @Parameter(description = "Driver's ID to be updated")
            @PathVariable("id") long id,
            @RequestBody Driver driver
    ) {
        Driver foundDriver = findDriver(id);

        foundDriver.setName(Optional.ofNullable(driver.getName()).orElse(foundDriver.getName()));
        foundDriver.setDateOfBirth(Optional.ofNullable(driver.getDateOfBirth()).orElse(foundDriver.getDateOfBirth()));

        return repository.save(foundDriver);
    }

    @PutMapping("/{id}")
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
    public Driver fullyUpdateDriver(
            @Parameter(description = "Driver's ID to be updated")
            @PathVariable("id") Long id,
            @RequestBody Driver driver
    ) {
        Driver foundDriver = findDriver(id);

        foundDriver.setName(driver.getName());
        foundDriver.setDateOfBirth(driver.getDateOfBirth());

        return repository.save(foundDriver);
    }

    @DeleteMapping("/{id}")
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
    public void deleteDriver(
            @Parameter(description = "Driver's ID to be updated")
            @PathVariable("id") Long id
    ) {
        repository.delete(findDriver(id));
    }
}
