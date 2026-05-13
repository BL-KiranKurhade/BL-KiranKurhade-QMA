package com.qma.conversion.controller;

import com.qma.conversion.entity.ConversionHistory;
import com.qma.conversion.repository.ConversionHistoryRepository;
import com.qma.conversion.service.ConversionService;
import com.qma.conversion.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// NOTE: @CrossOrigin intentionally omitted — CORS is handled by the API Gateway
@Tag(name = "Unit Conversion", description = "Convert values between units — UC21 Conversion Microservice")
@RestController
@RequestMapping("/api/convert")
public class ConversionController {

    @Autowired private ConversionService conversionService;
    @Autowired private ConversionHistoryRepository historyRepo;
    @Autowired private JwtUtil jwtUtil;

    // ── Convert ──────────────────────────────────────────────────────────────

    @Operation(
        summary = "Convert a value between units",
        description = "Performs conversion and saves the result to MySQL history.\n\n" +
            "Built-in LENGTH units: `mm`, `cm`, `in`, `ft`, `yd`, `m`, `km`, `mi`\n" +
            "Built-in WEIGHT units: `mg`, `g`, `oz`, `lb`, `kg`, `t`\n" +
            "Built-in VOLUME units: `tsp`, `tbsp`, `fl_oz`, `cup`, `ml`, `l`, `gal`\n" +
            "TEMPERATURE units: `C`, `F`, `K`",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Conversion result + history saved")
    @GetMapping
    public ResponseEntity<Map<String, Object>> convert(
            @Parameter(description = "Numeric value to convert", example = "100")
            @RequestParam double value,
            @Parameter(description = "Source unit, e.g. cm, kg, l", example = "cm")
            @RequestParam String from,
            @Parameter(description = "Target unit, e.g. in, lb, gal", example = "in")
            @RequestParam String to,
            @Parameter(description = "Category: LENGTH, WEIGHT, VOLUME, TEMPERATURE", example = "LENGTH")
            @RequestParam String category,
            HttpServletRequest request) {

        String username = jwtUtil.extractUsername(request);
        double result   = conversionService.convert(value, from, to, category, username);

        return ResponseEntity.ok(Map.of(
            "input",    value,
            "from",     from,
            "to",       to,
            "category", category,
            "result",   result,
            "username", username,
            "service",  "conversion-service"
        ));
    }

    // ── Conversion History ────────────────────────────────────────────────────

    @Operation(
        summary = "Get my conversion history",
        description = "Returns all past conversions for the authenticated user, newest first.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/history")
    public ResponseEntity<List<ConversionHistory>> getHistory(HttpServletRequest request) {
        String username = jwtUtil.extractUsername(request);
        return ResponseEntity.ok(historyRepo.findByUsernameOrderByCreatedAtDesc(username));
    }

    @Operation(
        summary = "Get all conversion history (admin)",
        description = "Returns every conversion record in the database.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/history/all")
    public ResponseEntity<List<ConversionHistory>> getAllHistory() {
        return ResponseEntity.ok(historyRepo.findAll());
    }

    // ── Health ────────────────────────────────────────────────────────────────

    @Operation(summary = "Health check — no auth required")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("conversion-service: UP");
    }
}
