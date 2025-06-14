# ─────────────────────────────────────────────
# 1) 빌드 스테이지
# ─────────────────────────────────────────────
FROM gradle:8.11.1-jdk21-jammy AS builder
WORKDIR /home/project

# 루트의 Gradle 설정만 먼저 복사
COPY settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle/ gradle/

# api 모듈 빌드에 필요한 서브프로젝트만 복사
COPY api/ api/
COPY application/ application/
COPY core/ core/
COPY lib/ lib/

# API 전용 캐시 사용
RUN --mount=type=cache,target=/root/.gradle,id=gradle-cache-api \
    gradle --no-daemon :api:bootJar -x test

# ─────────────────────────────────────────────
# 2) 런타임 스테이지
# ─────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /home/project/api/build/libs/api-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080
