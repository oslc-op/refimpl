# AGENTS.md — OSLC 2020 Reference Implementation

Follow human-facing docs: `DEVELOPMENT.md` (build/run/test) and `CONTRIBUTING.md` (rules).

Agent-specific notes:

- Default to `mvn test` (no Docker) for a build/compile check.
- The Docker-dependent acceptance suite (`mvn clean verify -Pacceptance`, plus the
  `server-rm` `*IT` tests under `verify`) requires Docker and should only be run
  when Docker is available.
- Prefer Java text blocks (`"""..."""`, JEP 378) for multi-line string literals
  (e.g. RDF/Turtle payloads in tests).
