plugins {
    `kotlin-dsl`
}

group = "dev.composereels.buildlogic"

// Compile the convention plugins against JDK 17 bytecode to match the app modules.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // The convention plugins program against the AGP / Kotlin / KSP plugin APIs.
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

// Register each convention plugin so modules can apply it by id.
gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "composereels.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "composereels.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "composereels.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "composereels.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidHilt") {
            id = "composereels.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("jvmLibrary") {
            id = "composereels.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
