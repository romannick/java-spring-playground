### Java Spring Boot monorepo with 2 microservices and a shared lib/module. Using flyway to manage DB migrations.

## Services

### User microservice
- Runs on port 8080
- Has /users CRUD API with tests

### Product microservice
- Runs on port 8081
- Has /products CRUD API with tests

## MySQL
- Has userService, productService users
## Prometheus
- Runs at http://localhost:9090
## Grafana
- Runs at http://localhost:3000 (user/pass = admin/admin)
## Jaeger
- Runs at http://localhost:16686

## Environments

### Local development 
- Requires mysql, prometheus.
- Can be started with `docker compose -f docker-compose-local.yml up`

### Dev environment
- Requires: mysql, prometheus, grafana, loki, promtail, nginx, user-service, product-service.
- Can be started with `docker compose -f docker-compose-dev.yml up --build`
- Services setup: 2 instances of both user-service and product-service with load balancer nginx
- Grafana setup: Loki integration, Dashbaords for each microservice that shows JVM and HTTP data.

