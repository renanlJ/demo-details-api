package io.devex.commerce.details.service;

import io.devex.commerce.details.model.ProductDetails;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

/** Deterministic in-memory product details repository. */
@Service
public class DetailsService {

  private final Map<String, ProductDetails> products = seed();

  public List<ProductDetails> list() {
    return products.values().stream()
        .sorted(Comparator.comparing(ProductDetails::productId))
        .toList();
  }

  public Optional<ProductDetails> find(String productId) {
    return Optional.ofNullable(products.get(productId));
  }

  private static Map<String, ProductDetails> seed() {
    var catalog = new LinkedHashMap<String, ProductDetails>();
    catalog.put(
        "SKU-1001",
        new ProductDetails(
            "SKU-1001",
            "Aurora Coffee Maker",
            "Programmable drip coffee maker with a thermal carafe, "
                + "24-hour brew timer and a reusable gold-tone filter.",
            "Home Appliances",
            new BigDecimal("129.99"),
            "USD",
            Map.of(
                "capacity", "1.8L",
                "power", "1100W",
                "color", "Matte Black",
                "warranty", "2 years")));
    catalog.put(
        "SKU-1002",
        new ProductDetails(
            "SKU-1002",
            "Nimbus Mechanical Keyboard",
            "Compact 75% mechanical keyboard with hot-swappable switches, "
                + "per-key RGB and tri-mode connectivity.",
            "Computer Accessories",
            new BigDecimal("89.50"),
            "USD",
            Map.of(
                "layout", "75%",
                "switches", "Hot-swappable linear",
                "connectivity", "Bluetooth 5.1 / USB-C",
                "battery", "4000mAh")));
    catalog.put(
        "SKU-1003",
        new ProductDetails(
            "SKU-1003",
            "Atlas Standing Desk",
            "Dual-motor sit-stand desk with four memory presets, "
                + "anti-collision sensors and a scratch-resistant worktop.",
            "Office Furniture",
            new BigDecimal("549.00"),
            "USD",
            Map.of(
                "dimensions", "160x80cm",
                "heightRange", "62-127cm",
                "motor", "Dual motor",
                "loadCapacity", "120kg")));
    return catalog;
  }
}
