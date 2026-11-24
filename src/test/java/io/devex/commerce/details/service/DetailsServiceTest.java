package io.devex.commerce.details.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DetailsServiceTest {

  private final DetailsService service = new DetailsService();

  @Test
  void findReturnsDetailsForKnownProduct() {
    var result = service.find("SKU-1001");

    assertThat(result).isPresent();
    var details = result.get();
    assertThat(details.productId()).isEqualTo("SKU-1001");
    assertThat(details.name()).isEqualTo("Aurora Coffee Maker");
    assertThat(details.category()).isEqualTo("Home Appliances");
    assertThat(details.price()).isNotNull();
    assertThat(details.currency()).isEqualTo("USD");
    assertThat(details.specifications()).containsKey("capacity");
  }

  @Test
  void findReturnsEmptyForUnknownProduct() {
    assertThat(service.find("SKU-9999")).isEmpty();
  }

  @Test
  void listReturnsAllProductsSorted() {
    var products = service.list();

    assertThat(products).hasSize(3);
    assertThat(products)
        .extracting("productId")
        .containsExactly("SKU-1001", "SKU-1002", "SKU-1003");
  }
}
