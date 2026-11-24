# demo-details-api

Product details microservice for the **Demo Commerce Platform** — a
polyglot reference workload for devex.io (Backstage-based IDP). Returns
name, description, category, price and specifications per product.

- **Language**: Java 21
- **Framework**: Spring Boot 3.5 (web, actuator, Micrometer/Prometheus,
  Micrometer-Tracing OTel), Maven build
- **Role**: leaf service consumed by `demo-product-api` — **required**
  (its failure produces 502 at the orchestrator)
- **Part of**: `system: demo-commerce` / `domain: commerce` (see `catalog-info.yaml`)

## API

| Method | Path | Description |
| ------ | ---- | ----------- |
| GET | `/api/v1/details` | Details for all products |
| GET | `/api/v1/details/{productId}` | Details for one product (404 if unknown) |
| GET | `/healthz` | Actuator health (liveness: `/healthz/liveness`) |
| GET | `/readyz` | Custom readiness (503 when `FAIL_READINESS=true`) |
| GET | `/metrics` | Prometheus scrape of `PrometheusMeterRegistry` |

OpenAPI contract: [`docs/api/openapi.yaml`](docs/api/openapi.yaml)

## Configuration

| Env var | Default | Description |
| ------- | ------- | ----------- |
| `PORT` | `8081` | `server.port` |
| `LOG_LEVEL` | `INFO` | Root logging level |
| `FAIL_READINESS` | `false` | Force `/readyz` → 503 |
| `OTEL_TRACING_ENABLED` | `false` | Enable OTLP trace export |
| `OTEL_EXPORTER_OTLP_ENDPOINT` | `http://localhost:4318` | Collector endpoint |

## Develop

Requires JDK 21 (`JAVA_HOME` must point at a JDK, not a JRE).

```bash
make install      # mvn dependency resolution
make test         # mvn test
make lint format  # mvn spotless:check (googleJavaFormat)
make verify       # full gate: spotless + tests + JaCoCo (>=80% lines)
make run          # spring-boot:run
```

## Docker

```bash
make docker-build
docker run -p 8081:8081 demo-details-api:0.1.0
```

## Kubernetes

```bash
kubectl apply -k deploy/kubernetes
```

## Observability

- JSON access logs via logbook+logstash encoder with `request_id` /
  `trace_id` (W3C `traceparent` propagated by Micrometer Tracing).
- Prometheus metrics on `/metrics` (request count, duration, status).

## Governance / CI

See `demo-product-api/docs/governance/scenarios.md` and
`.github/workflows/ci.yml` (spotless+verify+JaCoCo → OWASP dep-check+
gitleaks+trivy → package → image build+scan → GHCR on `v*` tags).
