plugins {
    id("org.springframework.boot")
    id("com.netflix.dgs.codegen") version "7.0.3"
}

dependencies {
    implementation(project(":application"))
    implementation(project(":lib"))

    // DGS Framework
    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:8.4.0"))
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")
    implementation("com.netflix.graphql.dgs:graphql-dgs-subscriptions-websockets-autoconfigure")

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // 개발 도구
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // 테스트 의존성
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.netflix.graphql.dgs:graphql-dgs-client")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// DGS 코드 생성 설정
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
