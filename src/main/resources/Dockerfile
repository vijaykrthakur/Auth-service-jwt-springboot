# Use Java 21
FROM eclipse-temurin:21-jdk

# Working directory
WORKDIR /app

# Copy project
COPY . .

# Build project
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Run jar
CMD ["java","-jar","target/*.jar"]
