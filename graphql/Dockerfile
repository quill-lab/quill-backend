# ─────────────────────────────────────────────
# 1) 빌드 스테이지
# ─────────────────────────────────────────────
FROM gradle:8.11.1-jdk21-jammy AS builder
WORKDIR /home/project

# 루트 Gradle 설정만 복사
COPY settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle/ gradle/


# graphql 모듈 빌드에 필요한 서브프로젝트만 복사
COPY graphql/ graphql/
COPY application/ application/
COPY core/ core/
COPY lib/ lib/

# 2) 의존성만 먼저 해결 (캐시 마운트)
RUN --mount=type=cache,target=/home/project/.gradle \
    gradle :graphql:bootJar --no-daemon --parallel --configure-on-demand -x test

RUN --mount=type=cache,target=/home/project/.gradle \
    gradle :graphql:bootJar --no-daemon -x test

# ─────────────────────────────────────────────
# 2) 런타임 스테이지
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /home/project/graphql/build/libs/graphql-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8081
