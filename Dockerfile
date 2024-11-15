FROM alpine:latest
RUN apk add --no-cache openjdk21
WORKDIR /app
COPY build/libs/app.jar app.jar

EXPOSE 8085
ENTRYPOINT ["java", "-jar","--add-opens=java.base/java.lang=ALL-UNNAMED", "app.jar"]
# Теперь сервис не компилируется в контейнере
# Перед тем как запустить docker-compose нужно выполнить таску ./gradlew bootJar
# Затем, чтобы не пересобирать все образы, а только этот - docker-compose build tracks-service
# Далее просто docker-compose up
