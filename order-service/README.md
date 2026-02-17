# Order Service - Microservice

## Overview
Independent order management microservice extracted from the storefront monolith application.

## Technology Stack
- **Spring Boot**: 3.4.2
- **Java**: 21
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Maven

## Dependencies
- Spring Web
- Spring Data JPA
- PostgreSQL Driver
- Lombok
- Validation
- Spring Boot DevTools

## Database Configuration
- **Database Name**: `orderdb`
- **Port**: 5432 (PostgreSQL default)
- **Credentials**: postgres/postgres (default - change for production)

## Application Configuration
- **Service Port**: 8081 (to avoid conflict with main storefront on 8080)
- **DDL Auto**: update (Hibernate will auto-create/update tables)

## Entities

### Orders
- `orderId` (String, PK)
- `userId` (String) - Reference to user in user service
- `timestamp` (LocalDateTime)
- `orderStatus` (OrderStatus enum)
- `totalPrice` (float)
- `paymentStatus` (PaymentStatus enum)
- `paymentMethod` (PaymentMethod enum)
- `shippingAddress` (String)
- `orderItems` (List<OrderItems>)

### OrderItems
- `orderItemId` (String, PK)
- `orderId` (FK to Orders)
- `itemId` (String) - Reference to item in catalog service
- `itemTitle` (String) - Denormalized for display
- `quantity` (Integer)
- `priceAtPurchase` (float)

### Enums
- **OrderStatus**: CREATED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, RETURNED
- **PaymentStatus**: PENDING, PAID, FAILED, REFUNDED
- **PaymentMethod**: CREDIT_CARD, DEBIT_CARD, CASH_ON_DELIVERY, Internet_Banking, UPI

## Independence Strategy
This service is **fully independent** from the monolith:
- No direct entity relationships to User or Items
- Uses String IDs (`userId`, `itemId`) instead of entity references
- Self-contained database schema
- Can be deployed and scaled separately

## Running the Service

### Prerequisites
1. PostgreSQL installed and running
2. Create database: `CREATE DATABASE orderdb;`
3. Java 21 installed
4. Maven installed

### Build
```bash
cd order-service
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

The service will start on **http://localhost:8081**

## Next Steps (Not Implemented Yet)
- [ ] Create OrderRepository
- [ ] Create OrderService
- [ ] Create OrderController (REST API)
- [ ] Add DTOs
- [ ] Add validation
- [ ] Implement order creation workflow
- [ ] Add pagination for order history
- [ ] Implement order status updates
