plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("com.gradleup.shadow") version "8.3.11"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.bxteam.org/releases")
    mavenCentral()
}

dependencies {
    api(project(":botloom-core"))
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation("org.bxteam.quark:paper:1.3.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

tasks.shadowJar {
    relocate("org.bxteam", "org.vaelow233.botloom.libs.bxteam")
    relocate("com.fasterxml", "org.vaelow233.botloom.libs.fasterxml")
    relocate("org.yaml", "org.vaelow233.botloom.libs.yaml")
    relocate("org.java_websocket", "org.vaelow233.botloom.libs.java_websocket")
    relocate("com.github.zafarkhaja", "org.vaelow233.botloom.libs.github.zafarkhaja")
}