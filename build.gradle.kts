plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    //kotlin("kapt") version "1.9.0"
    kotlin("jvm") version "2.0.0"
    java
}

shadow {
    group = "me.mcyeet"
    version = "1.0"
}

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")

    //maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://jitpack.io")

    //Jcloak
    //maven("https://artifacts.micartey.dev/public")
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    //Velocity
    //compileOnly("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    //kapt("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")

    //Plugins
    compileOnly("com.github.retrooper:packetevents-spigot:2.4.0")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    //compileOnly("me.clip:placeholderapi:2.11.4")

    //Commands
    compileOnly("dev.jorel:commandapi-bukkit-shade:9.4.2")
    //compileOnly("org.incendo:cloud-paper:2.0.0-beta.2")

    implementation("org.reflections:reflections:0.9.12")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)

    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.shadowJar {
    archiveClassifier.set("")

    //Dont shade kotlin, spigot downloads it at runtime :)
    dependencies {
        //Kotlin
        //exclude { it.moduleGroup == "org.jetbrains.kotlinx" }
        exclude { it.moduleGroup == "org.jetbrains.kotlin" }
        exclude { it.moduleGroup == "org.jetbrains" }
    }

    exclude("**/*.kotlin_builtins")
    exclude("**/*.kotlin_module")
    //minimize()
}