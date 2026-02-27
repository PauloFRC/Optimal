plugins {
    alias(libs.plugins.optimal.android.library)
    alias(libs.plugins.optimal.android.library.compose)
}

dependencies {
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
    api(libs.androidx.compose.material3.navigationSuite)
}
