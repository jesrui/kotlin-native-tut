// build.gradle.kts
plugins {
    kotlin("multiplatform") version "1.7.20"
}

repositories {
    mavenCentral()
}

kotlin {
    macosX64("nativeMac") { // on macOS
        binaries {
            executable()
        }
    }

    linuxX64("nativeLinux") { // on Linux
        binaries {
            executable()
        }
    }

    mingwX64("nativeWindows") { // on Windows
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

tasks.register("githubNativeOs") {
    val native = when (System.getenv("GITHUB_BUILD_OS")) {
        "ubuntu-latest" -> "nativeLinuxBinaries"
        "windows-latest" -> "nativeWindowsBinaries"
        "macos-latest" -> "nativeMacBinaries"
        else -> throw(IllegalArgumentException("please export GITHUB_BUILD_OS env.var."))
    }
    dependsOn(native)
}