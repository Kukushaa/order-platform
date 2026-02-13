FROM maven:3-eclipse-temurin-21 AS build
WORKDIR /app

# Copy all module POMs for dependency resolution
COPY pom.xml .
COPY api-gateway/pom.xml api-gateway/
COPY auth-service/pom.xml auth-service/
COPY email-sender-service/pom.xml email-sender-service/
COPY kafka-messages-sender-api/pom.xml kafka-messages-sender-api/
COPY orders-service/pom.xml orders-service/
COPY payment-service/pom.xml payment-service/
COPY product-service/pom.xml product-service/
COPY token-service/pom.xml token-service/
COPY users-service/pom.xml users-service/
COPY users-shared-lib/pom.xml users-shared-lib/

# Pre-download dependencies (layer cached when POMs unchanged)
RUN mvn dependency:resolve -pl payment-service -am -q 2>/dev/null; true

# Copy source and build
COPY . .
RUN mvn package -pl payment-service -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/payment-service/target/*.jar app.jar
EXPOSE 9002
ENTRYPOINT ["java", "-jar", "app.jar"]
