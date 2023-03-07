import gg.essential.gradle.util.noServerRunConfigs

plugins {
    java
    id("gg.essential.multi-version")
    id("gg.essential.defaults")
}

version = "2.0.0"
group = "me.sad.hideplayers"

base.archivesName.set("Hide Players-${platform.mcVersionStr}")

loom {
    noServerRunConfigs()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

