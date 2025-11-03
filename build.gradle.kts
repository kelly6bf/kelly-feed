import org.asciidoctor.gradle.jvm.AsciidoctorTask

plugins {
    id("java")
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.asciidoctor.jvm.convert") version "4.0.4"
}

group = "site.study"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// ✅ 올바른 configuration 이름
val asciidoctorExtensions: Configuration? by configurations.creating

dependencies {
    // Web
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Spring Data JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Querydsl
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    // MySQL
    runtimeOnly("com.mysql:mysql-connector-j")

    asciidoctorExtensions?.invoke("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.assertj:assertj-core:3.27.3")
}

val activeProfile = if (project.hasProperty("spring.profiles.active"))
    project.property("spring.profiles.active") as String
else
    "default"

val snippetsDir = file("build/generated-snippets")

tasks.test {
    useJUnitPlatform()
}

tasks.register<Test>("restDocsTest") {
    description = "Run only REST Docs related tests"
    group = "verification"
    outputs.dir(snippetsDir)
    useJUnitPlatform()
    filter {
        includeTestsMatching("*.docs.*")
    }
    onlyIf { activeProfile != "prod" }
}

tasks.named<AsciidoctorTask>("asciidoctor") {
    inputs.dir(snippetsDir)

    // ✅ REST Docs 테스트 먼저 수행
    dependsOn(tasks.named("restDocsTest"))

    // ✅ Asciidoctor 속성 설정
    baseDirFollowsSourceFile()
    attributes(
        mapOf(
            "snippets" to snippetsDir // ⚡ 이 부분이 핵심!
        )
    )

    sources {
        include("**/index.adoc")
    }

    setOutputDir(file("build/docs/asciidoc"))
    outputs.upToDateWhen { false }
    onlyIf { activeProfile != "prod" }
}

// ✅ HTML 복사 태스크
tasks.register<Delete>("cleanCopiedDocs") {
    delete("src/main/resources/static/docs")
}

tasks.register<Copy>("copyDocument") {
    dependsOn(tasks.named("cleanCopiedDocs"), tasks.named("asciidoctor"))
    from("build/docs/asciidoc")
    into("src/main/resources/static/docs")
}

// ✅ build 시 자동 복사
tasks.named("build") {
    dependsOn(tasks.named("copyDocument"))
}

// ✅ QueryDSL 설정
val querydslDir = layout.buildDirectory.dir("generated/querydsl").get().asFile
sourceSets["main"].java.srcDir(querydslDir)

tasks.withType<JavaCompile> {
    options.generatedSourceOutputDirectory = file(querydslDir)
}

tasks.named("clean") {
    doLast {
        querydslDir.deleteRecursively()
    }
}
