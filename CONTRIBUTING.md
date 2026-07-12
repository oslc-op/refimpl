# Contributing — OSLC 2020 Reference Implementation

Thanks for contributing! Please make sure to use latest SNAPSHOT versions of Lyo and 
Lyo Designer during development and testing of this repo.

## Before you submit

- Quick check (no Docker): `cd src && mvn test`.
- Full acceptance suite (needs Docker): `cd src && mvn clean verify -Pacceptance`.
- Keep `DEVELOPMENT.md` and `AGENTS.md` in sync whenever the test setup changes
  (the `acceptance` profile, Testcontainers / `docker-compose.yml` config, the
  `server-rm` `*IT` tests, etc.).
- Keep `DEVELOPMENT.md` and CI configuration in sync when CI config changes significantly.

## Updating docs

- User-facing docs should go to the README.md
- Dev-facing docs (useful for both devs and agents) should go to the DEVELOPMENT.md
- Universal contribution docs go here (CONTRIBUTING.md)
- Agent-specific docs go to AGENTS.md
