plugins {
    alias(libs.plugins.optimal.android.library)
}

android {
    namespace = "dev.optimal.tracker.core.model"
}

dependencies {
    implementation(project(":core:utils"))
}
