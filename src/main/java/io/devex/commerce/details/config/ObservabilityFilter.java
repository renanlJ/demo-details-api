package io.devex.commerce.details.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Propagates X-Request-ID and W3C traceparent into the MDC so every log line carries correlation
 * IDs, echoes the request ID back to the caller, and emits one structured access log per request.
 */
@Component
public class ObservabilityFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(ObservabilityFilter.class);
  private static final Pattern TRACEPARENT =
      Pattern.compile("^[0-9a-f]{2}-([0-9a-f]{32})-[0-9a-f]{16}-[0-9a-f]{2}$");

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    String requestId = request.getHeader("X-Request-ID");
    if (requestId == null || requestId.isBlank()) {
      requestId = UUID.randomUUID().toString().replace("-", "");
    }
    response.setHeader("X-Request-ID", requestId);
    MDC.put("request_id", requestId);

    String traceparent = request.getHeader("traceparent");
    if (traceparent != null) {
      Matcher m = TRACEPARENT.matcher(traceparent.toLowerCase());
      if (m.matches()) {
        MDC.put("trace_id", m.group(1));
      }
    }

    long start = System.nanoTime();
    try {
      chain.doFilter(request, response);
    } finally {
      long durationMs = (System.nanoTime() - start) / 1_000_000;
      MDC.put("method", request.getMethod());
      MDC.put("path", request.getRequestURI());
      MDC.put("status", String.valueOf(response.getStatus()));
      MDC.put("duration_ms", String.valueOf(durationMs));
      log.info("http request");
      MDC.clear();
    }
  }
}
