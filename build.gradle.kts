import dev.architectury.pack200.java.Pack200Adapter

plugins {
    java
    id("gg.essential.loom") version "0.10.0.4"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

version = "2.0.0-1.8.9"
group = "me.sad.hideplayers"

loom {
    runConfigs {
        named("client") {
            ideConfigGenerated(true)
        }
    }
    forge {
        pack200Provider.set(Pack200Adapter())
        mixinConfig("hideplayers.mixins.json")
    }
}

val includedFiles: Configuration by configurations.creating
configurations.implementation.get().extendsFrom(includedFiles)

repositories {
    maven("https://repo.spongepowered.org/maven/") {
        name = "Sponge"
    }
}

dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")
    compileOnly("org.spongepowered:mixin:0.8.5-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

tasks.processResources {
    inputs.property("version", version)
    inputs.property("mcversion", "1.8.9")

    filesMatching("mcmod.info") {
        expand("version" to version, "mcversion" to "1.8.9")
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.jar {
    archiveBaseName.set("Hide Players")
    archiveClassifier.set("original")
    manifest {
        attributes("FMLCorePluginContainsFMLMod" to "true")
        attributes("ForceLoadAsMod" to "true")
        attributes("TweakClass" to "org.spongepowered.asm.launch.MixinTweaker")
        attributes("TweakOrder" to "0")
        attributes("MixinConfigs" to "hideplayers.mixins.json")
    }
}

tasks.shadowJar {
    archiveBaseName.set("Hide Players")
    archiveClassifier.set("shadow")
    configurations = listOf(includedFiles)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.remapJar {
    dependsOn(tasks.shadowJar)
    archiveBaseName.set("Hide Players")
    input.set(tasks.shadowJar.get().archiveFile)
}