plugins {
    id("java")
    id("java-library")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.bxteam.org/releases")
    mavenCentral()
}

dependencies {
    api(project(":botloom-core"))
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