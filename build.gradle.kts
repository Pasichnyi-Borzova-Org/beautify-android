import Utils.getBuildCacheDir
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

// TODO: Remove once https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ben.manes)
    alias(libs.plugins.detekt)
    alias(libs.plugins.di.hilt) apply false
}

buildscript {
    dependencies {
        classpath(libs.gradle.navigation.safeargs.plugin)
    }
}

allprojects {
    apply(plugin = rootProject.project.libs.plugins.detekt.get().pluginId)

    // todo have bug https://github.com/detekt/detekt/issues/5485
    // https://detekt.dev/docs/gettingstarted/gradle/#kotlin-dsl-3
    detekt {
        toolVersion = rootProject.project.libs.versions.detekt.get()
        // parallel - creating a leak java process that will not close when the studio closed
        parallel = false
        allRules = true
        debug = false
        ignoreFailures = false
        config.setFrom(files("../detekt-lint/src/main/resources/config.yml"))
        source.from(
            files(
                "src/debug/java",
                "src/release/java",
                "src/debug/kotlin",
                "src/release/kotlin",
            ),
        )
    }

    dependencies {
        detektPlugins(rootProject.project.libs.detekt.formatting)
    }
}

tasks {
    register("clean", Delete::class) {
        delete(buildDir)
        delete(getBuildCacheDir())
    }
}

fun isStable(version: String): Boolean {
    val upperVersion = version.uppercase()
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any(upperVersion::contains)
    val regex = Regex("^[0-9,.v-]+(r)?$")
    return stableKeyword || regex.matches(version.lowercase())
}

tasks.withType<DependencyUpdatesTask> {
    checkForGradleUpdate = true
    rejectVersionIf {
        !isStable(candidate.version) && isStable(currentVersion)
    }
}
