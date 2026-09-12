plugins {
    id("java")
    id("java-library")
}

project.version = "1.0.0"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.bxteam.org/releases")
    mavenCentral()
}

dependencies {
    api(project(":botloom-core"))
    annotationProcessor("org.pf4j:pf4j:3.15.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.jar {
    manifest {
        attributes(
            "Plugin-Id" to "game-notifications",
            "Plugin-Version" to project.version.toString()
        )
    }
}