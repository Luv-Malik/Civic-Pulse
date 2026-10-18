FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/civicpulse-api-1.0.0.jar app.jar
EXPOSE 8080
CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
