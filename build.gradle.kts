plugins {
    id("java")
    id ("maven-publish")
}

buildscript {
    repositories {
        mavenCentral()
    }
}


group = "skywolf46"
version = properties["version"] as String

repositories {
    mavenCentral()
}

dependencies {
    // java dependencies
    compileOnly(files("V:/API/Java/Minecraft/Bukkit/Spigot/Spigot 1.12.2.jar"))
//    compileOnly(files("O:\\Servers\\우마공\\1.16 - 유튜브\\spigot-1.16.3.jar"))
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
    publications{
        create<MavenPublication>("jar"){
            from(components["java"])
            groupId = "skywolf46"
            artifactId = "commandannotation"
            version = properties["version"] as String
            pom{
                url.set("https://github.com/milkyway0308/CommandAnnotation.git")
            }
        }
    }
}