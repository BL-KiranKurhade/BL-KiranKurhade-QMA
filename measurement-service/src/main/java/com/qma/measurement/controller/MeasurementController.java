package com.qma.measurement.controller;

import com.qma.measurement.model.Measurement;
import com.qma.measurement.repository.MeasurementRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// NOTE: @CrossOrigin intentionally omitted — CORS is handled by the API Gateway
@Tag(name = "Measurements", description = "Measurement microservice CRUD — UC21")
@RestController
@RequestMapping("/api/measurements")
public class MeasurementController {

    @Autowired private MeasurementRepository repo;

    // ── Create ────────────────────────────────────────────────────────────────

    @Operation(summary = "Create a measurement")
    @RequestBody(
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Measurement.class),
            examples = @ExampleObject(value = "{\"value\":5.0,\"unit\":\"ft\",\"category\":\"LENGTH\"}")
        )
    )
    @ApiResponse(responseCode = "201", description = "Created successfully")
    @PostMapping
    public ResponseEntity<Measurement> create(
            @org.springframework.web.bind.annotation.RequestBody Measurement m) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(m));
    }

    // ── Read All / Filter by Category ─────────────────────────────────────────

    @Operation(
        summary = "Get all measurements",
        description = "Returns all measurements. Optionally filter by category: LENGTH, WEIGHT, VOLUME, TEMPERATURE."
    )
    @ApiResponse(responseCode = "200", description = "List returned")
    @GetMapping
    public ResponseEntity<List<Measurement>> getAll(
            @Parameter(description = "Category filter: LENGTH | WEIGHT | VOLUME | TEMPERATURE")
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(
            category != null ? repo.findByCategory(category.toUpperCase()) : repo.findAll()
        );
    }

    // ── Read One ──────────────────────────────────────────────────────────────

    @Operation(summary = "Get measurement by ID")
    @ApiResponse(responseCode = "200", description = "Found")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/{id}")
    public ResponseEntity<Measurement> getById(@PathVariable Long id) {
        return repo.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Operation(summary = "Update a measurement")
    @RequestBody(
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Measurement.class),
            examples = @ExampleObject(value = "{\"value\":10.5,\"unit\":\"m\",\"category\":\"LENGTH\"}")
        )
    )
    @ApiResponse(responseCode = "200", description = "Updated")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PutMapping("/{id}")
    public ResponseEntity<Measurement> update(
            @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody Measurement updated) {
        return repo.findById(id).map(existing -> {
            existing.setValue(updated.getValue());
            existing.setUnit(updated.getUnit());
            existing.setCategory(updated.getCategory());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @Operation(summary = "Delete a measurement")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @ApiResponse(responseCode = "404", description = "Not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ── Health ────────────────────────────────────────────────────────────────

    @Operation(summary = "Health check")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("measurement-service: UP");
    }
}
