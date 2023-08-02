import Utils.getKeystoreProperty
import Versions.BUILD_TOOLS
import Versions.VERSION_CODE
import Versions.VERSION_NAME
import io.gitlab.arturbosch.detekt.Detekt
import java.util.Locale

// TODO: Remove once https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    kotlin("kapt")
    kotlin("plugin.parcelize")
    kotlin("plugin.serialization")

    id(libs.plugins.di.hilt.get().pluginId)
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.kotlin.allopen)
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    // works only for 33 compileSdk
    // compileSdkExtension = libs.versions.compileSdkExtension.get().toInt()
    BUILD_TOOLS?.let { buildToolsVersion = it }
    testOptions.unitTests.isIncludeAndroidResources = true

    defaultConfig {
        applicationId = "com.opasichnyi.beautify"
        namespace = applicationId

        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionName = VERSION_NAME
        versionCode = VERSION_CODE

        // https://developer.android.com/guide/topics/resources/app-languages#gradle-config
        resourceConfigurations += "en"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    signingConfigs {
        getByName(Qualifier.BuildType.DEBUG) {
            storeFile = file("../keystore/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create(Qualifier.BuildType.RELEASE) {
            storeFile = getKeystoreProperty(rootProject, logger, "storeFile")?.let(::file)
                ?: getByName(Qualifier.BuildType.DEBUG).storeFile
            storePassword = getKeystoreProperty(rootProject, logger, "storePassword")
                ?: getByName(Qualifier.BuildType.DEBUG).storePassword
            keyAlias = getKeystoreProperty(rootProject, logger, "keyAlias")
                ?: getByName(Qualifier.BuildType.DEBUG).keyAlias
            keyPassword = getKeystoreProperty(rootProject, logger, "keyPassword")
                ?: getByName(Qualifier.BuildType.DEBUG).keyPassword
        }
    }

    buildTypes {
        getByName(Qualifier.BuildType.DEBUG) {
            signingConfig = signingConfigs.getByName(Qualifier.BuildType.DEBUG)
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
        }
        getByName(Qualifier.BuildType.RELEASE) {
            signingConfig = signingConfigs.getByName(Qualifier.BuildType.RELEASE)
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        targetCompatibility(libs.versions.jvmTarget.get().toInt())
        sourceCompatibility(libs.versions.jvmTarget.get().toInt())
    }

    kotlinOptions {
        jvmTarget = libs.versions.kotlinJvmTarget.get()
    }

    packaging.resources {
        excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }

    lint.checkDependencies = true
    lint.disable += "ContentDescription"

    val compileSourcesTask = task("compileSources")

    applicationVariants.all {
        val nameTask = this@all.name.replaceFirstChar {
            if (it.isLowerCase()) {
                it.titlecase(Locale.getDefault())
            } else {
                it.toString()
            }
        }
        val flavoredCompileSourcesTask = tasks.named("compile${nameTask}Sources").get()
        compileSourcesTask.dependsOn(flavoredCompileSourcesTask)
    }
}

tasks.withType<Detekt> { dependsOn(":detekt-lint:assemble") }

tasks.withType(Test::class) {
    allOpen {
        annotation("com.nix.mvvm.util.test.OpenForTesting")
    }

    testLogging {
        setExceptionFormat("full")
    }

    addTestListener(TestListenerImpl())
}

dependencies {
    // kotlin
    implementation(platform(libs.kotlin.bom))
    implementation(libs.bundles.kotlin.bom)

    // ui
    implementation(libs.bundles.arch)
    implementation(libs.bundles.ui)
    implementation(libs.bundles.views)
    implementation(libs.bundles.navigation)

    // frameworks
    implementation(libs.bundles.di)
    // TODO replace by KSP when will be supported (https://kotlinlang.org/docs/ksp-overview.html)
    kapt(libs.bundles.di.compiler)
    implementation(libs.bundles.multithreading)
    implementation(libs.bundles.network)
    implementation(libs.bundles.data)

    // utils
    coreLibraryDesugaring(libs.desugarJdk)
    implementation(libs.timber)
    debugImplementation(libs.leakcanaryDebug)
    implementation(libs.leakcanary)

    // test
    testImplementation(libs.bundles.test)

    // lint
    detektPlugins(libs.detekt.formatting)
    detektPlugins(project(":detekt-lint"))
}
