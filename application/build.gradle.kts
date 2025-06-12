import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.jpa")
    `java-library`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    sourceCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(project(":core"))
    implementation(project(":lib"))

    // 스프링 프레임워크
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")

    // JPA + Hibernate + 커넥션 풀
    implementation("jakarta.persistence:jakarta.persistence-api")
    implementation("org.springframework.data:spring-data-jpa")
    runtimeOnly("org.postgresql:postgresql")

    // Flyway 로 DB 마이그레이션
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    // Bean Validation API
    implementation("jakarta.validation:jakarta.validation-api")

    // 테스트
    testImplementation("org.springframework:spring-test")
    testImplementation("io.kotest:kotest-property:5.5.5")
    testImplementation("io.kotest:kotest-framework-datatest:5.5.5")
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
