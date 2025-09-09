# Build stage
FROM gradle:8.14.3-jdk17 AS builder
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN gradle build 

# Run stage
FROM openjdk:17-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app

# Automatically copy the built JAR without hardcoding name
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
