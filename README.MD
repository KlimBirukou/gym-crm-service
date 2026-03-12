# Gym CRM

## Build
before all
```bash 
./gradlew clean build
```

---

## Run
**local**
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```
```bash
docker compose --profile local up -d
```
**dev**
```bash
$env:SPRING_PROFILE="dev"; docker compose --profile dev up -d
```
**prod**
```bash
$env:SPRING_PROFILE="prod"; docker compose --profile prod up -d
```
**remove**
Use this command when switch between profiles to avoid connect to same db
```bash
docker compose --profile <profile> down -v 
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
