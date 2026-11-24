package io.devex.commerce.details.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Readiness probe aligned with the platform-wide {@code /readyz} convention. The {@code
 * FAIL_READINESS} env var forces a 503 for governance scenarios.
 */
@RestController
public class ReadinessController {

  private final boolean failReadiness;

  public ReadinessController(@Value("${app.fail-readiness:false}") boolean failReadiness) {
    this.failReadiness = failReadiness;
  }

  @GetMapping("/readyz")
  public ResponseEntity<Map<String, String>> readyz() {
    if (failReadiness) {
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
          .body(Map.of("status", "not ready"));
    }
    return ResponseEntity.ok(Map.of("status", "ready"));
  }
}
