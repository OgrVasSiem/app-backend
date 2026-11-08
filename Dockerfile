FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /workspace

COPY . .

RUN sed -i 's/\r$//' gradlew \
    && chmod +x gradlew \
    && ./gradlew --no-daemon :api:test :app:installDist


FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

RUN groupadd --gid 10001 backend \
    && useradd --uid 10001 --gid backend --no-create-home backend

COPY --from=build --chown=backend:backend \
    /workspace/app/build/install/backend/ /app/

USER backend

EXPOSE 8080

ENTRYPOINT ["/app/bin/backend"]