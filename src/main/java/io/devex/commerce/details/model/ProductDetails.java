package io.devex.commerce.details.model;

import java.math.BigDecimal;
import java.util.Map;

/** Product catalog details returned by the API. */
public record ProductDetails(
    String productId,
    String name,
    String description,
    String category,
    BigDecimal price,
    String currency,
    Map<String, String> specifications) {}
