// todo https://developer.android.com/build/releases/gradle-plugin#settings-plugin
// apply plugin 'com.android.settings'
// Note: The settings plugin currently only works in Groovy.

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = rootProject.projectDir.name
include(":app")
include(":detekt-lint")

buildCache {
    local {
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 7
    }
}
