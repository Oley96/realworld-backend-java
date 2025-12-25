FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /opt/realworld

COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon

COPY src ./src

RUN gradle clean build --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /opt/realworld

COPY --from=builder /opt/realworld/build/libs/*.jar app.jar

EXPOSE 1748
ENTRYPOINT ["java", "-jar", "/opt/realworld/app.jar"]
