import org.gradle.api.Project
import org.gradle.api.logging.Logger
import java.io.File
import java.io.FileInputStream
import java.util.Properties

object Utils {

    /**
     * Dir for build-cache from settings.gradle.kts
     */
    fun Project.getBuildCacheDir() = File(buildDir.parentFile, "build-cache")

    fun getKeystoreProperty(
        project: Project,
        logger: Logger,
        propertyName: String,
    ): String? {
        val keystorePropertiesFile = project.file("keystore.properties")
        val keystoreProperties = Properties()

        if (keystorePropertiesFile.exists()) {
            keystoreProperties.load(FileInputStream(keystorePropertiesFile))
        }

        return when {
            keystoreProperties.containsKey(propertyName) -> keystoreProperties[propertyName].toString()
            System.getenv(propertyName) != null -> System.getenv(propertyName)

            else -> {
                logger.error(
                    """
                    WARNING: Key store property '$propertyName' not found.
                    Will use signingConfigs.debug.
                """
                )

                null
            }
        }
    }
}