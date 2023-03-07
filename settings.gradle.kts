pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.minecraftforge.net")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://repo.essential.gg/repository/maven-public/")
    }
    plugins {
        val egtVersion = "0.1.7"
        id("gg.essential.multi-version.root") version egtVersion
    }
}

rootProject.name = "HidePlayers"
rootProject.buildFileName = "root.gradle.kts"

listOf(
    "1.8.9",
    "1.12.2"
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}