plugins {
    alias(libs.plugins.optimal.android.library)
    alias(libs.plugins.optimal.android.room)
    alias(libs.plugins.optimal.hilt)
}

android {
    namespace = "dev.optimal.tracker.core.database"
}

dependencies {
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    implementation(project(":core:model"))
}