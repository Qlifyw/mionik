plugins {
    kotlin("jvm") version "1.3.61"
    `maven-publish`
}

group = "org.mionik"
version = "1.6.2"

repositories {
    mavenCentral()
}

object Versions {
    const val JUPITER_VERSION = "5.6.2"
    const val JACKSON_VERSION = "2.10.1"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    testCompile("com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.JACKSON_VERSION}") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
    }

    testCompile("org.junit.jupiter:junit-jupiter-api:${Versions.JUPITER_VERSION}")
    testCompile("org.junit.jupiter:junit-jupiter-engine:${Versions.JUPITER_VERSION}")
    testCompile("org.junit.jupiter:junit-jupiter-params:${Versions.JUPITER_VERSION}")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    test {
        useJUnitPlatform()
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Qlifyw/mionik")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}