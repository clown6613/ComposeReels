plugins {
    alias(libs.plugins.composereels.android.library)
    alias(libs.plugins.composereels.android.hilt)
}

android {
    namespace = "dev.composereels.core.data"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.common)
    implementation(projects.core.network)

    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}
