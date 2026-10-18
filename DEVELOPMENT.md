# Development — OSLC 2020 Reference Implementation

Reference implementation of four OSLC servers — Requirements (RM), Change (CM),
Quality (QM), Architecture (AM) — managed as a Maven reactor under `src/`.

Reactor modules: `lib-common`, `server-rm`, `server-cm`, `server-qm`, `server-am`,
and `client-toolchain` (Kotlin). Acceptance tests live in `refimpl-tests`, which is
gated behind the `acceptance` Maven profile.

## Prerequisites

- JDK 21 (25 recommended)
- Maven 3.9

## Build (no Docker needed)

    cd src
    mvn clean install          # builds all modules and installs them
    mvn test                 # compile/unit check only

The `acceptance` profile (adds `refimpl-tests`) is **off by default**.

## Tests

### Without Docker (default)

    mvn test

Runs a compile/unit check and requires no Docker. By default `refimpl-tests` is not
built, and `server-rm`'s `*IT` integration tests only execute under `verify`, so no
Docker-dependent tests run.

### With Docker (acceptance suite)

The acceptance tests start the four servers (ports **8800–8803**) via
`refimpl-tests/src/test/resources/docker-compose.yml` (Testcontainers) and exercise
them. They include `OslcTest` and `SwaggerTest` (in `refimpl-tests`) and the
`RequirementsIT` / `SpCatalogIT` tests (in `server-rm`).

    # requires Docker
    mvn clean verify -Pacceptance

> Do **not** run `mvn verify` (or `-Pacceptance`) without Docker — the
> integration tests will fail because the servers are not started.

## Running the servers manually

    # built-in Jetty (one terminal per server)
    cd src/server-rm && mvn clean jetty:run-war   # :8800
    cd src/server-cm && mvn clean jetty:run-war   # :8801
    cd src/server-qm && mvn clean jetty:run-war   # :8802
    cd src/server-am && mvn clean jetty:run-war   # :8803

    # or all four via Docker Compose
    cd src && docker compose up --build

    # or pre-built images, e.g.
    docker run -p 8800:8080 ghcr.io/oslc-op/refimpl/server-rm:latest

Once running, populate sample data with `client-toolchain` (Kotlin): run
`src/client-toolchain/src/main/kotlin/co/oslc/refimpl/client/Main.kt`, or
`java -jar client-toolchain/target/client-toolchain.jar`.

Service roots are at `http://localhost:<port>/services/rootservices`.
