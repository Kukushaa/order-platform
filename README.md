# Order Platform

A microservices based order management platform built with Spring Boot.
The system handles authentication, users, products, orders, payments,
email notifications, and API routing through a gateway. Services
communicate through REST and Kafka events. PostgreSQL stores data. Redis
supports rate limiting. Stripe handles payments.

------------------------------------------------------------------------

## Architecture

The platform follows a microservices architecture with an API Gateway as
the entry point.

Services: - api-gateway - auth-service - product-service -
payment-service - users-service - orders-service -
email-sender-service - kafka-messages-sender-api - token-service -
users-shared-lib

------------------------------------------------------------------------

## Tech Stack

Backend: - Java 21 - Spring Boot 3.5 - Spring Cloud - Spring Security -
OAuth2 Resource Server

Infrastructure: - PostgreSQL - Kafka - Redis

Payments: - Stripe SDK

Build: - Maven

------------------------------------------------------------------------

## Service Ports

Service           Port
  ----------------- ------
API Gateway       8080
Auth Service      9000
Product Service   9001
Payment Service   9002
Users Service     9003
Orders Service    9004

------------------------------------------------------------------------

## API Gateway Routes

Route                   Service
  ----------------------- -----------------
/auth/\*\*              auth-service
/api/v1/products/\*\*   product-service
/api/v1/payments/\*\*   payment-service
/api/v1/users/\*\*      users-service
/api/v1/orders/\*\*     orders-service

------------------------------------------------------------------------

## Security

-   JWT authentication
-   JWK validation via auth-service
-   Access token lifetime: 15 minutes
-   Refresh token lifetime: 7 days

------------------------------------------------------------------------

## Rate Limiting

Implemented in API Gateway using Redis.

Defaults: - replenishRate: 10 - burstCapacity: 20 - keyResolver: IP
address

------------------------------------------------------------------------

## Database

PostgreSQL database:

products_management

Default connection:

jdbc:postgresql://localhost:5432/products_management

Default credentials:

username: usr password: password

------------------------------------------------------------------------

## Kafka

Bootstrap server:

localhost:9092

Usage: - Auth service produces events - Payment service produces
events - Orders service produces events - Email sender consumes events

------------------------------------------------------------------------

## Local Development Setup

Required software: - Java 21 - Maven - PostgreSQL - Kafka - Redis

------------------------------------------------------------------------

## Build Project

From repository root:

mvn clean install

------------------------------------------------------------------------

## Run Services

Start infrastructure first: - PostgreSQL - Kafka - Redis

Run services individually:

mvn -pl api-gateway spring-boot:run mvn -pl auth-service spring-boot:run
mvn -pl product-service spring-boot:run mvn -pl payment-service
spring-boot:run mvn -pl users-service spring-boot:run mvn -pl
orders-service spring-boot:run mvn -pl email-sender-service
spring-boot:run

------------------------------------------------------------------------

## Configuration Files

Locations:

api-gateway/src/main/resources/application.yml
auth-service/src/main/resources/application.yml
product-service/src/main/resources/application.yml
payment-service/src/main/resources/application.yml
users-service/src/main/resources/application.yml
orders-service/src/main/resources/application.yml
email-sender-service/src/main/resources/application.properties

------------------------------------------------------------------------

## Required Configuration Changes

Before running the platform:

Update database credentials. Set Stripe API key in payment-service. Set
SMTP credentials in email-sender-service.

------------------------------------------------------------------------

## Module Responsibilities

api-gateway Routes requests, validates JWT, applies rate limiting.

auth-service Handles authentication and token generation.

product-service Manages product APIs.

payment-service Processes payments through Stripe.

users-service Manages user data.

orders-service Handles order lifecycle.

email-sender-service Consumes Kafka events and sends emails.

users-shared-lib Shared user models and persistence support.

kafka-messages-sender-api Shared Kafka message contracts.

token-service Token utilities and JOSE support.

------------------------------------------------------------------------

## License

MIT