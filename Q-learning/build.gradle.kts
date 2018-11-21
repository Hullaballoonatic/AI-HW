import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Params {
    const val tornadofxVersion = "1.7.17"
}

plugins {
    java
    application
    kotlin("jvm") version "1.3.10"
}

group = "AI-HW"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compile("no.tornado", "tornadofx", Params.tornadofxVersion)
    compile(kotlin("stdlib-jdk8"))
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}