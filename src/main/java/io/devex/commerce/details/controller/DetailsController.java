package io.devex.commerce.details.controller;

import io.devex.commerce.details.model.ProductDetails;
import io.devex.commerce.details.service.DetailsService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** REST endpoints for product details. */
@RestController
@RequestMapping("/api/v1/details")
public class DetailsController {

  private final DetailsService service;

  public DetailsController(DetailsService service) {
    this.service = service;
  }

  @GetMapping
  public List<ProductDetails> list() {
    return service.list();
  }

  @GetMapping("/{productId}")
  public ProductDetails byProduct(@PathVariable String productId) {
    return service
        .find(productId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
  }
}
