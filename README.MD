# Gym CRM

## Build

```bash
./gradlew clean build
```

### build without running tests

```bash
./gradlew clean build -x test
```

---

## Run

### Local

Infrastructure in Docker, app runs from IDE (check custom run command in build.gradle)

```bash
docker compose -f compose.local.yaml up -d
```

```bash
./gradlew bootRun
```

Stop containers

```bash
docker compose -f compose.local.yaml stop
```

Stop and remove containers with volumes

```bash
docker compose -f compose.local.yaml down -v
```

---

### Dev

Full stack in Docker. Build JAR first:

```bash
docker compose -f compose.dev.yaml up -d --build
```

Stop containers

```bash
docker compose -f compose.dev.yaml stop
```

Stop and remove containers with volumes

```bash
docker compose -f compose.dev.yaml down -v
```

---

### Prod

App + DB in Docker. Build JAR first:

```bash
docker compose -f compose.prod.yaml up -d --build
```

Stop containers

```bash
docker compose -f compose.prod.yaml stop
```

Stop and remove containers with volumes

```bash
docker compose -f compose.prod.yaml down -v
```

---

## Profiles

| Service    | local   | dev    | prod   |
|------------|---------|--------|--------|
| PostgreSQL | Docker  | Docker | Docker |
| App        | **IDE** | Docker | Docker |
| Prometheus | Docker  | Docker | —      |
| Grafana    | Docker  | Docker | —      |

| Feature         | local | dev     | prod               |
|-----------------|-------|---------|--------------------|
| SQL logging     | full  | compact | off                |
| Log level (app) | DEBUG | DEBUG   | INFO               |
| Actuator        | all   | all     | health, prometheus |
| Swagger         | ✅     | ✅       | ❌                  |
| Seed data       | ✅     | ✅       | ❌                  |

---

## URLs

| Service    | URL                                   |
|------------|---------------------------------------|
| App        | http://localhost:8080                 |
| Swagger    | http://localhost:8080/swagger-ui.html |
| Actuator   | http://localhost:8080/actuator        |
| Prometheus | http://localhost:9090                 |
| Grafana    | http://localhost:3000 `admin / admin` |
