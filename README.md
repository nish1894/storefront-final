# Storefront

E-commerce platform built with Spring Boot + Thymeleaf, structured as two independent microservices.

## Project Structure

```
storefront-final/
├── storefront-service/   # Main storefront (port 8080)
│   ├── src/
│   ├── pom.xml
│   ├── mvnw / mvnw.cmd
│   └── package.json      # Tailwind CSS tooling
│
├── order-service/         # Order management (port 8081)
│   ├── src/
│   ├── pom.xml
│   └── mvnw / mvnw.cmd
│
└── README.md
```

## Running Locally

Each service runs independently:

```bash
# Storefront (port 8080)
cd storefront-service
./mvnw spring-boot:run

# Order Service (port 8081)
cd order-service
./mvnw spring-boot:run
```

## Building

```bash
cd storefront-service && ./mvnw clean package -DskipTests
cd order-service && ./mvnw clean package -DskipTests
```

## Environment Variables

| Variable | Service | Description |
|---|---|---|
| `PORT` | both | HTTP port (Render injects this automatically) |
| `SPRING_PROFILES_ACTIVE` | both | Set to `prod` for production |
| `SPRING_DATASOURCE_URL` | both | PostgreSQL JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | both | Database username |
| `SPRING_DATASOURCE_PASSWORD` | both | Database password |
| `ORDER_SERVICE_URL` | storefront-service | Public URL of order-service on Render |
| `CORS_ALLOWED_ORIGINS` | order-service | Comma-separated allowed origins (storefront URL) |

## Tech Stack

- Java 21, Spring Boot 3.4.2
- Thymeleaf + Tailwind CSS + Flowbite
- PostgreSQL (separate databases per service)
- REST communication between services

## Render Deployment

### Deployment Order

Deploy **order-service first**, then **storefront-service** (storefront needs the order-service URL).

### Service 1: order-service

| Setting | Value |
|---|---|
| **Root Directory** | `order-service` |
| **Build Command** | `./mvnw clean package -DskipTests` |
| **Start Command** | `java -jar target/order-service-0.0.1-SNAPSHOT.jar` |

Environment variables:
```
PORT=10000
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:postgresql://<render-db-host>:5432/orderdb
SPRING_DATASOURCE_USERNAME=<db-user>
SPRING_DATASOURCE_PASSWORD=<db-password>
CORS_ALLOWED_ORIGINS=https://<storefront-service>.onrender.com
```

### Service 2: storefront-service

| Setting | Value |
|---|---|
| **Root Directory** | `storefront-service` |
| **Build Command** | `./mvnw clean package -DskipTests` |
| **Start Command** | `java -jar target/scm2.0-0.0.1-SNAPSHOT.jar` |

Environment variables:
```
PORT=10000
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:postgresql://<render-db-host>:5432/storefrontdb
SPRING_DATASOURCE_USERNAME=<db-user>
SPRING_DATASOURCE_PASSWORD=<db-password>
ORDER_SERVICE_URL=https://<order-service>.onrender.com
```
