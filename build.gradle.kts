import dev.architectury.pack200.java.Pack200Adapter

plugins {
    java
    id("gg.essential.loom") version "0.10.0.4"
    id("dev.architectury.architectury-pack200") version "0.1.3"
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
    }
}

dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.processResources {
    inputs.property("version", version)
    inputs.property("mcversion", "1.8.9")

    filesMatching("mcmod.info") {
        expand("version" to version, "mcversion" to "1.8.9")
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.remapJar {
    archiveBaseName.set("Hide Players")
}
