# Stage 1: Build jar file using mave
# Base Image for maven project
FROM maven:3.9.5-eclipse-temurin-17 AS builder

# set working directory in container is /app
WORKDIR /app

# Copy pom.xml và tải dependencies trước để cache được
COPY pom.xml .

# Chạy lệnh tải dependencies trước (tận dụng cache tốt)
RUN mvn dependency:go-offline

# Sau đó copy phần src vào
COPY src ./src

# Build jar file of spring boot application
RUN mvn clean package -DskipTests

# Stage 2: Run spring boot application using a slim JDK image
FROM eclipse-temurin:17-jdk-alpine

# set working directory in container is /app
WORKDIR /app

# Copy the built jar from the builder (in stage 1)
COPY --from=builder /app/target/*.jar quanlydoantotnghiep.jar

# Expose container port
EXPOSE 8080

## Run application when container started
#ENTRYPOINT ["java", "-jar", "quanlydoantotnghiep.jar"]