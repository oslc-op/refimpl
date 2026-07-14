# AGENTS.md — OSLC 2020 Reference Implementation

Follow human-facing docs: `DEVELOPMENT.md` (build/run/test) and `CONTRIBUTING.md` (rules).

Agent-specific notes:

- Default to `mvn test` (no Docker) for a build/compile check.
- The Docker-dependent acceptance suite (`mvn clean verify -Pacceptance`, plus the
  `server-rm` `*IT` tests under `verify`) requires Docker and should only be run
  when Docker is available.

## Generated code — Lyo Designer guard blocks

Most Java source files under `src/server-*/` are **generated** by
[Lyo Designer](https://github.com/eclipse/lyo.designer). The generator
overwrites everything **outside** the guard comments on each regeneration:

```
// Start of user code <blockName>
// End of user code
```

**Only modify code that sits between these two comments.**  
Never change initialisation statements, method signatures, field
declarations, imports, or any other generated boilerplate that lives
outside a `// user code` block — those changes will be silently lost the
next time someone runs the generator.

If you need to introduce a new import required by your user-code changes,
place it inside the `// Start of user code imports` … `// End of user code`
block near the top of the file.
