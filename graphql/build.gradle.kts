plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("com.netflix.dgs.codegen") version "7.0.3"
    id("com.google.cloud.tools.jib")
}

dependencies {
    implementation(project(":application"))
    implementation(project(":lib"))
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter")
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
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
        image = "webdev0594/gow-graphql"
        tags = setOf("latest", version.toString())
        auth {
            username = System.getenv("DOCKER_HUB_USERNAME")
            password = System.getenv("DOCKER_HUB_PASSWORD")
        }
    }
    container {
        jvmFlags = listOf("-Xms1024m", "-Xmx1024m")
        ports = listOf("8081")
        environment = mapOf(
            "SPRING_PROFILES_ACTIVE" to "prod"
        )
        creationTime.set("USE_CURRENT_TIMESTAMP")
    }
}

tasks.withType<com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask> {
    generateClient = true
    packageName = "lab.ujumeonji.literaturebackend.graphql.generated"
    typeMapping = mutableMapOf(
        "DateTime" to "java.time.LocalDateTime",
        "UUID" to "java.util.UUID"
    )
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
