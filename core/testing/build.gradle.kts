plugins {
    alias(libs.plugins.composereels.android.library)
}

android {
    namespace = "dev.composereels.core.testing"
}

dependencies {
    // Test fixtures are exposed transitively to consuming test source sets.
    api(projects.core.model)
    api(projects.core.data)

    api(libs.kotlinx.coroutines.test)
    api(libs.junit4)
}
