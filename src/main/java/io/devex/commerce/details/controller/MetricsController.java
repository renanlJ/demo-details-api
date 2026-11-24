package io.devex.commerce.details.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

/**
 * Prometheus scrape endpoint aligned with the platform-wide {@code /metrics} convention. Delegates
 * to the Micrometer Prometheus registry directly instead of relying on actuator endpoint exposure.
 */
@RestController
public class MetricsController {

  private final PrometheusMeterRegistry registry;

  public MetricsController(PrometheusMeterRegistry registry) {
    this.registry = registry;
  }

  @GetMapping("/metrics")
  public ResponseEntity<String> metrics() {
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType("text/plain; version=0.0.4; charset=utf-8"))
        .body(registry.scrape());
  }
}
