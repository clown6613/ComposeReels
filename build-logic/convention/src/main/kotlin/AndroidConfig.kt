import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/** Single source of truth for the SDK levels used across every module. */
object AndroidConfig {
    const val COMPILE_SDK = 35
    const val TARGET_SDK = 35

    // Android 8.0. Picked so adaptive launcher icons work without raster fallbacks and so the
    // full Compose + Media3 feature set is available; still covers the vast majority of devices.
    const val MIN_SDK = 26
    val JAVA_VERSION = JavaVersion.VERSION_17
}

/**
 * Apply the common Android + Kotlin settings shared by application and library modules
 * (SDK levels, Java/Kotlin toolchain, packaging).
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = AndroidConfig.COMPILE_SDK

        defaultConfig {
            minSdk = AndroidConfig.MIN_SDK
        }

        compileOptions {
            sourceCompatibility = AndroidConfig.JAVA_VERSION
            targetCompatibility = AndroidConfig.JAVA_VERSION
        }
    }

    // Align the Kotlin JVM target with the Java compatibility level.
    extensions.configure(KotlinAndroidProjectExtension::class.java) {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = AndroidConfig.JAVA_VERSION.toString()
        targetCompatibility = AndroidConfig.JAVA_VERSION.toString()
    }
}
