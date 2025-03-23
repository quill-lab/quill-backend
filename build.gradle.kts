plugins {
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
    id("com.google.cloud.tools.jib") version "3.4.4"
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
}

allprojects {
    group = "lab.ujumeonji"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

// 공통 설정을 서브프로젝트에 적용
subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("io.spring.dependency-management")
    }

    // 모든 모듈에 공통으로 적용되는 Java 버전 설정
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
        sourceCompatibility = JavaVersion.VERSION_21
    }

    // 공통 Kotlin 컴파일러 옵션 설정
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "21"
        }
    }

    // 공통 테스트 설정
    tasks.withType<Test> {
        useJUnitPlatform()
    }

    // 공통 의존성 관리
    dependencyManagement {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.3")
        }
    }

    // 모든 모듈에 공통으로 적용되는 의존성
    dependencies {
        // Kotlin 관련 공통 의존성
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        
        // 테스트 관련 공통 의존성
        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testImplementation("org.junit.jupiter:junit-jupiter-engine")
        testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
        testImplementation("io.kotest:kotest-assertions-core:5.5.5")
        testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
        testImplementation("io.mockk:mockk:1.13.4")
        testImplementation("com.h2database:h2")
    }
}
