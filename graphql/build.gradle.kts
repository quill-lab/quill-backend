plugins {
    id("org.springframework.boot")
    id("com.netflix.dgs.codegen") version "7.0.3"
}

dependencies {
    implementation(project(":application"))
    implementation(project(":core"))
    implementation(project(":lib"))
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter")
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = true
}
tasks.named<Jar>("jar") {
    enabled = false
}

tasks.withType<com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask> {
    generateClient = true
    packageName = "lab.ujumeonji.literaturebackend.graphql.generated"
    typeMapping =
        mutableMapOf(
            "DateTime" to "java.time.OffsetDateTime",
            "UUID" to "java.util.UUID",
        )
}
