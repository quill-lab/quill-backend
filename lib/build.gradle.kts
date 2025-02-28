import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    sourceCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework:spring-context")

    // Spring Web 관련 의존성
    implementation("org.springframework:spring-web")
    implementation("org.springframework:spring-webmvc")
    implementation("org.springframework.security:spring-security-core")
    implementation("jakarta.servlet:jakarta.servlet-api")
    implementation("jakarta.validation:jakarta.validation-api")

    // JWT 관련 의존성
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    // Swagger 관련 의존성
    implementation("io.swagger.core.v3:swagger-annotations:2.2.8")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
    testImplementation("io.kotest:kotest-assertions-core:5.5.5")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
    testImplementation("io.mockk:mockk:1.13.4")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JvmTarget.JVM_21.target
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

tasks.getByName<Jar>("jar") {
    enabled = true
}
