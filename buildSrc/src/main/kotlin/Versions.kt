import org.gradle.api.Project

/**
 * @return build tools version from project property
 * Property instantiated in ci/.gitlab-ci-before.yml
 * */
private fun Project.getBuildToolsVersion(): String? =
    if (hasProperty("buildToolsVersion")) {
        properties["buildToolsVersion"].toString().replace("-test", "")
    } else {
        System.getProperty("buildToolsVersion", null)
    }

/**
 * @return version name from project property, if exist
 * Property instantiated in ci/.gitlab-ci-before.yml
 * */
private fun Project.getVersionName(): String =
    if (hasProperty("versionName")) properties["versionName"].toString() else "offline"

/**
 * @return version code from project property, if exist
 * Property instantiated in ci/.gitlab-ci-before.yml
 * */
private fun Project.getVersionCode(): Int =
    if (hasProperty("versionCode")) properties["versionCode"].toString().toInt() else 1

fun Project.isCI(): Boolean =
    hasProperty("versionName")

object Versions {

    val Project.VERSION_NAME get() = getVersionName()
    val Project.VERSION_CODE get() = getVersionCode()
    val Project.BUILD_TOOLS get() = getBuildToolsVersion()
}
