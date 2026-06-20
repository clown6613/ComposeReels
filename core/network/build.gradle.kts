plugins {
    alias(libs.plugins.composereels.android.library)
    alias(libs.plugins.composereels.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.composereels.core.network"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.common)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit4)
}
