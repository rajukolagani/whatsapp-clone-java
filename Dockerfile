# Use a standard Java 17 image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the build files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Copy the source code
COPY src ./src

# Build the project using the Maven wrapper
RUN ./mvnw package -DskipTests

# Set the command to run the application
ENTRYPOINT ["java", "-jar", "target/whatsapp-clone-0.0.1-SNAPSHOT.jar"]