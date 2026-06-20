import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/** Convention plugin for Android library modules that expose Compose UI. */
class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("composereels.android.library")
            apply("org.jetbrains.kotlin.plugin.compose")
        }

        extensions.configure<LibraryExtension> {
            configureAndroidCompose(this)
        }
    }
}
