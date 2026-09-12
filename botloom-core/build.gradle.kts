plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

dependencies {
    api("org.vaelow233.botweave:botweave-connector:1.0.0")
    api("com.fasterxml.jackson.core:jackson-databind:2.22.2")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.22.2")
    api("org.pf4j:pf4j:3.15.1")
    api("org.slf4j:slf4j-api:2.0.17")
    compileOnlyApi("org.jdbi:jdbi3-core:3.39.1")
    compileOnlyApi("org.jdbi:jdbi3-sqlobject:3.39.1")
    compileOnly("org.flywaydb:flyway-core:9.22.3")
    compileOnly("com.zaxxer:HikariCP:4.0.3")
    compileOnly("org.xerial:sqlite-jdbc:3.53.4.0")
    compileOnly("com.mysql:mysql-connector-j:26.7.0")
    compileOnly("org.flywaydb:flyway-mysql:9.22.3")
    compileOnly("org.postgresql:postgresql:42.7.13")
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

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}