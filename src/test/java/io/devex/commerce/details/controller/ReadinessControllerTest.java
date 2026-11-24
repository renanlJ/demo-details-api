package io.devex.commerce.details.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ReadinessControllerTest {

  @Test
  void readyzReturnsOkWhenHealthy() {
    var controller = new ReadinessController(false);

    var response = controller.readyz();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).containsEntry("status", "ready");
  }

  @Test
  void readyzReturns503WhenFailReadinessSet() {
    var controller = new ReadinessController(true);

    var response = controller.readyz();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    assertThat(response.getBody()).containsEntry("status", "not ready");
  }
}
