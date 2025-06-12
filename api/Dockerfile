# ─────────────────────────────────────────────
# 1) 빌드 스테이지
# ─────────────────────────────────────────────
FROM gradle:8.11.1-jdk21-jammy AS builder
WORKDIR /home/project

# 루트의 Gradle 설정만 먼저 복사
COPY settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle/ gradle/

# 의존성만 미리 내려받아 캐시에 저장
RUN --mount=type=cache,target=/home/project/.gradle \
    gradle dependencies --no-daemon

# api 모듈 빌드에 필요한 서브프로젝트만 복사
COPY api/ api/
COPY application/ application/
COPY core/ core/
COPY lib/ lib/

RUN --mount=type=cache,target=/home/project/.gradle \
    gradle :api:bootJar --no-daemon -x test

# ─────────────────────────────────────────────
# 2) 런타임 스테이지
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 빌드 스테이지에서 생성된 fat-jar 만 복사
COPY --from=builder /home/project/api/build/libs/api-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
