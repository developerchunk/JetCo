// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    id("com.vanniktech.maven.publish") version "0.29.0" apply false
    id("com.gradleup.nmcp") version "0.0.7" apply false
}

buildscript {
    dependencies {
        classpath(libs.dokka.gradle.plugin)
    }
}

apply("versions.gradle")

//val artifactId = getArtifactId(project)
//println("Artifact ID for jetco: $artifactId")

tasks.register<Delete>("clean") { delete(rootProject.layout.buildDirectory) }
