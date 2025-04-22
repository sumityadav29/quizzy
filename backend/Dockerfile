FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY backend /app

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/quizzy-0.0.1-SNAPSHOT.jar"]