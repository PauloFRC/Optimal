plugins {
    alias(libs.plugins.optimal.android.library)
    alias(libs.plugins.optimal.android.library.compose)
    alias(libs.plugins.optimal.hilt)
}

android {
    namespace = "dev.optimal.tracker.core.ui"
}

dependencies {
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.svg)
    implementation(libs.androidx.material3)

    implementation(project(":core:designsystem"))
}

