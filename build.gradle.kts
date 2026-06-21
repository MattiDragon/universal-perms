plugins {
    alias(libs.plugins.loom)
    alias(libs.plugins.mod.publish)
    `maven-publish`
}

val modVersion: String by project
version = modVersion
val mavenGroup: String by project
group = mavenGroup

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    minecraft(libs.minecraft)
    implementation(libs.fabric.loader)

    implementation(libs.fabric.api)
}

loom.runs.configureEach {
    runDirectory = layout.projectDirectory.dir("run").dir(name)
}

tasks.processResources {
    inputs.property("version", project.version)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(mapOf("version" to project.version))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

java {
    withSourcesJar()
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${base.archivesName}" }
    }
}

publishMods {
    val mcVersion = libs.minecraft.get().version!!

    file.set(tasks.jar.get().archiveFile)
    additionalFiles.from(tasks["sourcesJar"])

    displayName = "v${modVersion} [${libs.versions.minecraft.get()}]"
    changelog = providers.fileContents(layout.projectDirectory.file("changelog/$modVersion+$mcVersion.md")).asText

    type.set(providers.environmentVariable("RELEASE_TYPE").map { me.modmuss50.mpp.ReleaseType.of(it) })
    modLoaders.add("fabric")

    dryRun = providers.gradleProperty("publish_dry_run").isPresent

    modrinth {
        projectId = "GyrkNvLS"
        accessToken.set(providers.environmentVariable("MODRINTH_TOKEN"))

        requires("fabric-api")
        minecraftVersions.add(providers.environmentVariable("MODRINTH_MC_VERSION").filter { it.isNotBlank() }.orElse(mcVersion))
    }

    github {
        repository = "MattiDragon/universal-perms"
        accessToken.set(providers.environmentVariable("GITHUB_TOKEN"))

        commitish.set(providers.environmentVariable("GITHUB_BRANCH"))
        tagName.set(version.map { it.replace('+', '-') })
    }
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
