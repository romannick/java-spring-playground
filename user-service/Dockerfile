FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .

COPY shared/ shared/
COPY gateway-service /app/gateway-service
COPY user-service /app/user-service
COPY product-service /app/product-service

RUN mvn -B clean install -pl shared -am -DskipTests
RUN mvn -B -pl user-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/user-service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]