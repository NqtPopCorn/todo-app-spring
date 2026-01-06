# ==========================================
# STAGE 1: Build Frontend (React)
# ==========================================
FROM node:20-alpine AS frontend-builder
WORKDIR /app/frontend

# Install dependencies
COPY frontend/package.json frontend/package-lock.json ./
# Use npm ci for clean install based on package-lock.json
RUN npm ci

# Build the frontend
COPY frontend/ ./
RUN npm run build


# ==========================================
# STAGE 2: Build Backend (Spring Boot)
# ==========================================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app/backend

# Install dependencies and build the backend
COPY backend/pom.xml .
COPY backend/src ./src
RUN mvn clean package -DskipTests


# ==========================================
# STAGE 3: Run (Final Image)
# ==========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=frontend-builder /app/frontend/dist ./static
COPY --from=builder /app/backend/target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]