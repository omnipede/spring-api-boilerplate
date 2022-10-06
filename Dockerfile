FROM adoptopenjdk:11-jdk as builder

WORKDIR /srv

# Copy gradle requirements
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src src

# Build
RUN chmod +x ./gradlew
RUN ./gradlew build -x test

FROM adoptopenjdk:11-jdk

# Move build results
COPY --from=builder /srv/build/libs/*.jar server.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "server.jar"]