# Architecture

## Service boundaries

| Service | Owns |
| --- | --- |
| Dashboard | Route navigation, camera and selection state, and rendering the latest API snapshot |
| Fleet API | HTTP and WebSocket boundary, gRPC deadlines and DTO mapping, and PostgreSQL persistence for fleet workflows |
| Simulation engine | Grid bounds, robot identity and telemetry, and simulation status |
| PostgreSQL | Durable fleet workflow data, starting with the task table |

The engine creates immutable Phase 1 state at startup. Java reads that state over gRPC and maps generated messages into REST records. The dashboard keeps one Pinia store so selections from the canvas and robot list point to the same data. Robot IDs, positions, battery levels, headings, and statuses come from the engine. Shelf and zone placements are visual scene geometry. Phase 1 validates grid bounds and has no shelf occupancy or collision model.

## Coordinates and headings

The simulation is a zero-indexed rectangular grid. X ranges from 0 through width minus one, and Y ranges from 0 through height minus one. Simulation X maps to Three.js X, simulation Y maps to Three.js Z, and Three.js Y is elevation. An integer grid position is the center of a floor cell. Heading 0 faces positive simulation Y, with positive heading rotating toward positive simulation X.

## Communication and failure behavior

The dashboard requests health, robots, and status on entry and on explicit refresh. Vite and nginx proxy those requests and WebSocket upgrades to the Java API. Direct clients can use the API port with configurable CORS. The WebSocket endpoint provides the handshake for future updates. Java reuses a managed gRPC channel and applies a deadline to each call. A failed engine health check degrades the health response independently of the HTTP API. An upstream robot or status failure returns HTTP 503. The browser surfaces missing data and lets the operator retry.

This setup uses plaintext gRPC on the local development network. Compose gates API startup on successful PostgreSQL and engine health checks. Flyway manages the SQL schema, and Hibernate validates it at startup.

## Code conventions

Vue components use `<script setup>` to declare typed props and events. Pinia derives the selected robot from a robot ID and the current snapshot, so a refresh keeps the selection when that ID is still present. TresJS maps Vue scene components to Three.js objects.

Spring creates services and injects their dependencies through constructors. Java records define the JSON response shapes. The generated gRPC stub translates requests and responses on the shared channel, which Spring closes when the application shuts down.

C++ stores the fixed-size fleet by value in a `std::array` and uses an `enum class` for domain statuses. gRPC's server factory returns an owning smart pointer. The synchronous service serves a read-only state snapshot and requires no application-managed worker threads.
