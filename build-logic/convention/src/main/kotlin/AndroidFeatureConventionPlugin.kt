import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin for feature modules. A feature is a Compose library with Hilt
 * plus the shared core modules every feature needs (model, ui, designsystem).
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("composereels.android.library.compose")
            apply("composereels.android.hilt")
        }

        dependencies {
            add("implementation", project(":core:model"))
            add("implementation", project(":core:ui"))
            add("implementation", project(":core:designsystem"))

            add("implementation", libs.findLibrary("androidx-lifecycle-runtime-compose").get())
            add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
            add("implementation", libs.findLibrary("androidx-hilt-navigation-compose").get())
            add("implementation", libs.findLibrary("androidx-navigation-compose").get())
        }
    }
}
