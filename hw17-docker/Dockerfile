FROM bellsoft/liberica-openjdk-alpine-musl:17.0.13
COPY /target/hw17-docker-*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]