# Stage 1: build
# Start with a Maven image that includes JDK 17
FROM maven:3.9.9-amazoncorretto-17-debian AS build

# Copy source code and pom.xml file to /app folder
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build source code with maven
RUN mvn package -DskipTests

# Stage 2: create image
FROM amazoncorretto:17.0.13

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]


# Build command: docker build -t <image name>:<version> .
# Run command: docker run -d <image name>
# Run command: docker run --name <container name> -p <port>:<port> -e <environment variables> <image name>
# Tạo mạng: docker network create my-network
# Chạy container: docker run -d --name kafka --network=my-network -p 9092:9092 kafka-image