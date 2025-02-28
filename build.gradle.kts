plugins {
    id("org.springframework.boot") version "3.4.1"
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
