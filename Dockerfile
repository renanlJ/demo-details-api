# syntax=docker/dockerfile:1
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /src
COPY pom.xml ./
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B -DskipTests package

FROM eclipse-temurin:21-jre-alpine
LABEL org.opencontainers.image.title="demo-details-api" \
      org.opencontainers.image.description="Product details service - Demo Commerce Platform" \
      org.opencontainers.image.source="https://github.com/renanlJ/demo-details-api" \
      org.opencontainers.image.licenses="MIT"
RUN addgroup -S app && adduser -S app -G app
USER app
WORKDIR /app
COPY --from=build /src/target/demo-details-api-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
