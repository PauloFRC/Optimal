plugins {
    alias(libs.plugins.optimal.android.library)
    alias(libs.plugins.optimal.android.library.compose)
}

android {
    namespace = "dev.optimal.tracker.core.designsystem"
}

dependencies {
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
    api(libs.androidx.compose.material3.navigationSuite)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.ui.text.google.fonts)
}
