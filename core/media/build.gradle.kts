plugins {
    alias(libs.plugins.composereels.android.library)
    alias(libs.plugins.composereels.android.hilt)
}

android {
    namespace = "dev.composereels.core.media"
}

dependencies {
    implementation(projects.core.model)

    // Exposed via ReelPlayerController.player, so consumers need these on their compile classpath.
    api(libs.media3.exoplayer)
    api(libs.media3.common)

    implementation(libs.media3.exoplayer.hls)
    implementation(libs.media3.exoplayer.dash)
}
