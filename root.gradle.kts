plugins {
    kotlin("jvm") version "1.6.0" apply false // required for essential multiversion
    id("gg.essential.multi-version.root")
}

preprocess {
    "1.12.2"(11202, "srg") {
        "1.8.9"(10809, "srg", file("versions/1.12.2-1.8.9.txt"))
    }
}