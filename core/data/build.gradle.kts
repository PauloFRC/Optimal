plugins {
    alias(libs.plugins.optimal.android.library)
    alias(libs.plugins.optimal.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "dev.optimal.tracker.core.data"
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:model"))
}
