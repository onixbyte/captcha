import java.net.URI

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("signing")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

val artefactVersion: String by project

group = "com.onixbyte"
version = artefactVersion

repositories {
    mavenCentral()
}

dependencies {
    api(libs.jhlabs.core)
    implementation(libs.jspecify.core)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platformLauncher)
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("captcha") {
            groupId = group.toString()
            artifactId = "captcha"
            version = artefactVersion

            pom {
                name = "Onixbyte Captcha"
                description = "A clone of Google Kaptcha."
                url = "https://github.com/onixbyte/captcha"

                licenses {
                    license {
                        name = "MIT"
                        url = "https://github.com/onixbyte/captcha/blob/main/LICENSE"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com:onixbyte/captcha.git"
                    developerConnection = "scm:git:git://github.com:onixbyte/captcha.git"
                    url = "https://github.com/onixbyte/captcha"
                }

                developers {
                    developer {
                        name = "penggle"
                        url = "https://github.com/penggle"
                    }

                    developer {
                        name = "imac-beep"
                        url = "https://github.com/imac-beep"
                    }

                    developer {
                        name = "Trydofor"
                        url = "https://github.com/trydofor"
                    }

                    developer {
                        id = "zihluwang"
                        name = "zihluwang"
                        timezone = "Asia/Hong_Kong"
                        url = "https://github.com/zihluwang"
                    }
                }
            }

            from(components["java"])

            signing {
                sign(publishing.publications["captcha"])
            }
        }

        repositories {
            maven {
                name = "sonatypeNexus"
                url = URI(providers.gradleProperty("repo.maven-central.host").get())
                credentials {
                    username = providers.gradleProperty("repo.maven-central.username").get()
                    password = providers.gradleProperty("repo.maven-central.password").get()
                }
            }
        }
    }
}