# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Development (live reload at http://localhost:8080)
./mvnw quarkus:dev

# Run tests
./mvnw test                           # Unit tests
./mvnw failsafe:integration-test      # Integration tests

# Build
./mvnw package                        # Standard JAR (target/quarkus-app/quarkus-run.jar)
./mvnw package -Dnative               # Native executable (requires GraalVM)
./mvnw package -Dnative -Dquarkus.native.container-build=true  # Native via Docker
```

## Architecture

**Stack:** Quarkus 3.32.3 · Java 25 · Maven · PostgreSQL · Renarde · Panache · Qute templates · Tailwind CSS

### Code Layout

```
src/main/java/
├── com/move/intranet/   REST API endpoints (standard Quarkus REST + Jackson)
├── model/               Domain entities (Panache Next)
├── rest/                Renarde web controllers (extend Controller, server-side rendering)
└── util/                Qute template extensions + startup data seeder

src/main/resources/
├── templates/           Qute HTML templates (main.html = base layout)
├── application.properties
└── import.sql           Full PostgreSQL schema (authoritative domain model)
```

### Two Web Layers

The app has two parallel approaches to serving HTTP:
1. **Renarde controllers** (`rest/`) — server-side rendered pages using Qute templates. Routes follow `ClassName/methodName` convention. Form validation via `@NotBlank` with automatic error propagation to templates.
2. **REST resources** (`com/move/intranet/`) — JSON API endpoints for programmatic access.

### Domain (defined in `import.sql`)

Vehicle fleet management intranet for Brazilian companies:
- `empresas` — companies (CNPJ-based)
- `pessoas` / `colaboradores` — people and their employee relationships
- `veiculos` / `veiculo_uso` — fleet vehicles and usage logs
- `veiculo_rodizio` — parking rotation rules by license plate ending
- `locais` — origin/destination points
- All tables use `deleted_at` for soft deletes

### Key Patterns

- **Startup seeding:** `util/Startup.java` uses `@Observes StartupEvent` to seed demo data in dev mode.
- **Template extensions:** `util/JavaExtensions.java` adds methods callable in Qute templates (e.g., `{todo.task.capitalise}`).
- **Dev UI:** Available at `http://localhost:8080/q/dev/` when running in dev mode.
