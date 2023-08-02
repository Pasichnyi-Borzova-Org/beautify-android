plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    System.setProperty("buildToolsVersion", libs.versions.buildToolsVersion.get())
}