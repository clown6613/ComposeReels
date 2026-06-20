plugins {
    alias(libs.plugins.composereels.android.feature)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.composereels.feature.reels"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.media)

    // PlayerView for attaching the shared player surface.
    implementation(libs.media3.ui)

    // Type-safe navigation routes are serialized.
    implementation(libs.kotlinx.serialization.json)

    testImplementation(projects.core.testing)
    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
}
