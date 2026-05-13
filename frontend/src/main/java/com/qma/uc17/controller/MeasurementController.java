package com.qma.uc17.controller;

import com.qma.uc17.dto.MeasurementDTO;
import com.qma.uc17.service.MeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Measurements", description = "CRUD for quantity measurements — UC17 Spring Boot Backend")
@RestController
@RequestMapping("/api/measurements")
@CrossOrigin(origins = "*")
public class MeasurementController {

    private final MeasurementService service;

    @Autowired
    public MeasurementController(MeasurementService service) {
        this.service = service;
    }

    @Operation(summary = "Create a measurement", description = "Saves a new measurement to the database")
    @RequestBody(
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = MeasurementDTO.class),
            examples = @ExampleObject(
                name = "Length example",
                value = "{\"value\":5.0,\"unit\":\"FT\",\"category\":\"LENGTH\"}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Measurement created",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = MeasurementDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<MeasurementDTO> create(
            @org.springframework.web.bind.annotation.RequestBody MeasurementDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @Operation(summary = "Get all measurements",
        description = "Returns all measurements. Filter by category: LENGTH, WEIGHT, VOLUME, TEMPERATURE")
    @ApiResponse(responseCode = "200", description = "List of measurements")
    @GetMapping
    public ResponseEntity<List<MeasurementDTO>> getAll(
        @Parameter(description = "Category filter e.g. LENGTH, WEIGHT")
        @RequestParam(required = false) String category) {
        List<MeasurementDTO> result = (category != null)
            ? service.getByCategory(category)
            : service.getAll();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get measurement by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = MeasurementDTO.class))),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<MeasurementDTO> getById(
        @Parameter(description = "Measurement ID") @PathVariable Long id) {
        return service.getById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update a measurement")
    @RequestBody(
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = MeasurementDTO.class),
            examples = @ExampleObject(value = "{\"value\":10.0,\"unit\":\"IN\",\"category\":\"LENGTH\"}")
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Updated successfully"),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<MeasurementDTO> update(
        @Parameter(description = "Measurement ID") @PathVariable Long id,
        @org.springframework.web.bind.annotation.RequestBody MeasurementDTO dto) {
        return service.update(id, dto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a measurement")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @Parameter(description = "Measurement ID") @PathVariable Long id) {
        return service.delete(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
    }
}
