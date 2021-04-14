plugins {
    id("java")
    id("maven-publish")
}

buildscript {
    repositories {
        mavenCentral()
    }
}


group = "skywolf46"
version = properties["version"] as String

tasks {
    processResources {
        outputs.upToDateWhen({ false })
        filesMatching("plugin.yml") {
            expand("version" to project.properties["version"])
        }
    }
    test {
        useJUnit()
        maxHeapSize = "1G"
    }
}
repositories {
    mavenCentral()
    maven(properties["reposilite.spigot"] as String)
    maven(properties["reposilite.release"] as String)
}

dependencies {
    // java dependencies
    compileOnly("org.spigotmc:spigot:1.12.2")
    compileOnly("org.projectlombok:lombok:1.18.16")
    annotationProcessor("org.projectlombok:lombok:1.18.16")
    testImplementation("junit:junit:4.13")

}

publishing {
    repositories {
        maven {
            name = "Reposilite"
            url = uri(properties["reposilite.release"] as String)
            credentials {
                username = properties["reposilite.user"] as String
                password = properties["reposilite.token"] as String
            }
        }
    }
    publications {
        create<MavenPublication>("jar") {
            from(components["java"])
            groupId = "skywolf46"
            artifactId = "commandannotation"
            version = properties["version"] as String
            pom {
                url.set("https://github.com/milkyway0308/CommandAnnotation.git")
            }
        }
    }
}