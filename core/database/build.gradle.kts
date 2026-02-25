plugins {
    // 1. Swapped the custom plugin for the standard Android Library plugin
    id("com.android.library")
    // Remember: No kotlin.android plugin needed here either for AGP 9.0+!
}

android {
    namespace = "dev.optimal.tracker.core.database"

    // 2. Added back the required SDK versions
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    // 3. Added Java compatibility just to be safe with the built-in Kotlin compiler
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // 4. implementation is recognized again
    implementation(libs.androidx.core.ktx)
}