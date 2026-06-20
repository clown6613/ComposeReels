plugins {
    alias(libs.plugins.composereels.android.library.compose)
}

android {
    namespace = "dev.composereels.core.designsystem"
}

dependencies {
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.icons.extended)
    api(libs.androidx.compose.ui)
}
