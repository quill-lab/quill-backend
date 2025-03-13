plugins {
    id("org.springframework.boot")
    id("com.netflix.dgs.codegen") version "7.0.3"
}

dependencies {
    implementation(project(":application"))
    implementation(project(":lib"))

    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:"))
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter")
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.netflix.graphql.dgs:graphql-dgs-client")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask> {
    generateClient = true
    packageName = "lab.ujumeonji.literaturebackend.graphql.generated"
    typeMapping = mutableMapOf(
        "DateTime" to "java.time.LocalDateTime",
        "UUID" to "java.util.UUID"
    )
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = true
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
