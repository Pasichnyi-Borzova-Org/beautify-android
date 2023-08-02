// remove after close https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("java-library")
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get().toInt())
    targetCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get().toInt())
}

dependencies {
    detektPlugins(libs.detekt.formatting)
    compileOnly(libs.detekt.api)
    implementation(libs.detekt.cli)
}