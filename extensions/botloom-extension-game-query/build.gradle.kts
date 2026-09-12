plugins {
    id("java")
    id("java-library")
}

project.version = "1.0.0"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.bxteam.org/releases")
    maven("https://repo.lucko.me/")
    mavenCentral()
}

dependencies {
    api(project(":botloom-core"))
    annotationProcessor("org.pf4j:pf4j:3.15.1")
    compileOnly("me.lucko:spark-api:0.1-SNAPSHOT")
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
            "Plugin-Id" to "game-query",
            "Plugin-Version" to project.version.toString()
        )
    }
}