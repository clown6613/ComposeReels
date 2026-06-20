plugins {
    alias(libs.plugins.composereels.android.library.compose)
}

android {
    namespace = "dev.composereels.core.ui"
}

dependencies {
    api(projects.core.model)
    api(projects.core.designsystem)

    implementation(libs.coil.compose)
    implementation(libs.androidx.compose.material.icons.extended)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
