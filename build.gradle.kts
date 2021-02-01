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
        expand("version" to project.properties["version"])
    }
    test {
        useJUnit()
        maxHeapSize = "1G"
    }
}




repositories {
    mavenCentral()
}

dependencies {
    // java dependencies
    implementation(files("V:/API/Java/Minecraft/Bukkit/Spigot/Spigot 1.12.2.jar"))
    compileOnly("org.projectlombok:lombok:1.18.16")
    annotationProcessor("org.projectlombok:lombok:1.18.16")
    testImplementation("junit:junit:4.13")

}

publishing {
    repositories {
        maven {
            name = "Github"
            url = uri("https://maven.pkg.github.com/milkyway0308/CommandAnnotation")
            credentials {
                username = properties["gpr.user"] as String
                password = properties["gpr.key"] as String
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