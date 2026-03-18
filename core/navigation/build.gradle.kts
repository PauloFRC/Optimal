plugins {
    alias(libs.plugins.optimal.android.library)
    alias(libs.plugins.optimal.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "dev.optimal.tracker.core.navigation"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
}

