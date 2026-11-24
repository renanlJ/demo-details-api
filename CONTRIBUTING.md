# Contributing

## Development setup

Requires JDK 21 and Maven 3.8+.

```sh
mvn test          # unit + integration tests
mvn verify        # tests + spotless + JaCoCo gate + package
mvn spring-boot:run
```

`make` targets wrap the same commands but a local `make` is not required.

## Conventions

- Code is formatted with `spotless` (google-java-format). Run
  `mvn spotless:apply` before pushing.
- Commits: imperative mood, concise subject line.
- Update `CHANGELOG.md` under `Unreleased` for user-visible changes.

## Pull requests

- CI must be green: `mvn verify` (tests, spotless, JaCoCo >= 70% line
  coverage), Trivy image scan.
- Keep PRs focused; link related issues.
