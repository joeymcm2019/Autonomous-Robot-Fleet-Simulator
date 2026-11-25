# Simulation engine

Use a C++20 compiler with CMake, gRPC, Protobuf, and GoogleTest. Run `cmake -S . -B build`, `cmake --build build`, and `ctest --test-dir build --output-on-failure`. Start with `build/simulation-engine` and check it with `build/simulation-engine --health-check`. The server binds to loopback on port 50051 by default. See the [root README](../../README.md) for environment settings and Compose.
