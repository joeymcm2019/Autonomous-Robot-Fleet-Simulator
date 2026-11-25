FROM debian:bookworm-slim AS build
RUN apt-get update && apt-get install -y --no-install-recommends \
    cmake g++ make libgrpc++-dev libprotobuf-dev protobuf-compiler \
    protobuf-compiler-grpc libgtest-dev && rm -rf /var/lib/apt/lists/*
WORKDIR /workspace
COPY proto proto
COPY services/simulation-engine services/simulation-engine
RUN cmake -S services/simulation-engine -B /build -DCMAKE_BUILD_TYPE=Release \
    && cmake --build /build --parallel 2 \
    && ctest --test-dir /build --output-on-failure

FROM debian:bookworm-slim
RUN apt-get update && apt-get install -y --no-install-recommends \
    libgrpc++1.51 libprotobuf32 && rm -rf /var/lib/apt/lists/*
COPY --from=build /build/simulation-engine /usr/local/bin/simulation-engine
ENV SIMULATION_BIND_HOST=0.0.0.0
USER 65532:65532
EXPOSE 50051
ENTRYPOINT ["simulation-engine"]
