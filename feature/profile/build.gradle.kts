plugins {
    alias(libs.plugins.optimal.android.library)
    alias(libs.plugins.optimal.android.library.compose)
    alias(libs.plugins.optimal.hilt)
}

android {
    namespace = "dev.optimal.tracker.feature.profile"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(project(":core:navigation"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
}
