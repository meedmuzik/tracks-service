FROM alpine:latest AS build
RUN apk add gradle && apk add openjdk21
COPY . .
RUN gradle bootJar

FROM alpine:latest AS result
RUN apk add openjdk21
WORKDIR /app
COPY --from=build /build/libs/*.jar app.jar

EXPOSE 8070
ENTRYPOINT ["java", "-jar", "app.jar"]
