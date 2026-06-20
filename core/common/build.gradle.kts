plugins {
    alias(libs.plugins.composereels.android.library)
    alias(libs.plugins.composereels.android.hilt)
}

android {
    namespace = "dev.composereels.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}
