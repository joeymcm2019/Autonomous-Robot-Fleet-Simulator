FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace
COPY proto proto
COPY services/fleet-api services/fleet-api
WORKDIR /workspace/services/fleet-api
RUN ./mvnw -B package

FROM eclipse-temurin:21-jre-jammy
RUN apt-get update && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /workspace/services/fleet-api/target/fleet-api-0.1.0.jar app.jar
USER 65532:65532
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
