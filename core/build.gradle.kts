import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation(project(":lib"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.github.f4b6a3:uuid-creator:6.0.0")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    implementation("net.ttddyy.observation:datasource-micrometer-spring-boot:1.0.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
    runtimeOnly("org.postgresql:postgresql")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
    testImplementation("io.kotest:kotest-assertions-core:5.5.5")
    testImplementation("io.kotest:kotest-property:5.5.5")
    testImplementation("io.kotest:kotest-framework-datatest:5.5.5")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
    testImplementation("io.mockk:mockk:1.13.4")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget = JvmTarget.JVM_21
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
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
