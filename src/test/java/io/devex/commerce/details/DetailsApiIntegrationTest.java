package io.devex.commerce.details;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DetailsApiIntegrationTest {

  @Autowired private TestRestTemplate rest;

  @Test
  void listDetails() {
    var response = rest.getForEntity("/api/v1/details", String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("SKU-1001", "SKU-1002", "SKU-1003");
  }

  @Test
  void getDetailsByProduct() {
    var response = rest.getForEntity("/api/v1/details/SKU-1002", String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody())
        .contains("Nimbus Mechanical Keyboard", "Computer Accessories", "89.5");
  }

  @Test
  void getDetailsUnknownProductReturns404() {
    var response = rest.getForEntity("/api/v1/details/SKU-9999", String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void healthAndReadinessProbes() {
    assertThat(rest.getForEntity("/healthz", String.class).getStatusCode())
        .isEqualTo(HttpStatus.OK);
    assertThat(rest.getForEntity("/readyz", String.class).getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(rest.getForEntity("/healthz/liveness", String.class).getStatusCode())
        .isEqualTo(HttpStatus.OK);
    assertThat(rest.getForEntity("/healthz/readiness", String.class).getStatusCode())
        .isEqualTo(HttpStatus.OK);
  }

  @Test
  void prometheusMetricsEndpoint() {
    var response = rest.getForEntity("/metrics", String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("http_server_requests_seconds");
  }

  @Test
  void requestIdIsEchoedOnResponse() {
    var headers = new HttpHeaders();
    headers.set("X-Request-ID", "java-req-7");
    var response =
        rest.exchange("/api/v1/details", HttpMethod.GET, new HttpEntity<>(headers), String.class);

    assertThat(response.getHeaders().getFirst("X-Request-ID")).isEqualTo("java-req-7");
  }
}
