plugins {
    id("org.springframework.boot")
    id("com.google.cloud.tools.jib")
    id("com.netflix.dgs.codegen") version "7.0.3"
}

dependencies {
    implementation(project(":application"))
    implementation(project(":lib"))

    // 웹 관련 의존성 유지
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // API 문서화 도구 유지
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

    // 모니터링 및 추적 의존성 유지
    implementation("io.micrometer:micrometer-tracing")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")

    // 개발 도구
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // 테스트 의존성
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

jib {
    from {
        image = "eclipse-temurin:21-jre-alpine"
        platforms {
            platform {
                architecture = "amd64"
                os = "linux"
            }
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    to {
        image = "webdev0594/gow-api"
        tags = setOf("latest", version.toString())
        auth {
            username = System.getenv("DOCKER_HUB_USERNAME")
            password = System.getenv("DOCKER_HUB_PASSWORD")
        }
    }
    container {
        jvmFlags = listOf("-Xms1024m", "-Xmx1024m")
        ports = listOf("8080")
        environment =
            mapOf(
                "SPRING_PROFILES_ACTIVE" to "prod",
            )
        creationTime.set("USE_CURRENT_TIMESTAMP")
    }
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = true
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
