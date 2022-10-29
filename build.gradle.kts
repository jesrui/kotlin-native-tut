// build.gradle.kts
plugins {
    kotlin("multiplatform") version "1.7.20"
}

repositories {
    mavenCentral()
}

kotlin {
    // macosX64("native") { // on macOS
    linuxX64("native") { // on Linux
    // mingwX64("native") { // on Windows
        binaries {
            executable()
        }
    }

    sourceSets {
        commonMain {
             dependencies {
                 implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
             }
        }
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "7.5.1"
    distributionType = Wrapper.DistributionType.BIN
}