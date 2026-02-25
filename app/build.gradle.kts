
import dev.optimal.tracker.convention.OptimalBuildType

plugins {
    alias(libs.plugins.optimal.android.application)
    alias(libs.plugins.optimal.android.application.compose)
    alias(libs.plugins.optimal.hilt)
    alias(libs.plugins.kotlin.serialization)
}

// TODO: Secure Keystore Loading

android {
    compileSdk = 35
    namespace = "dev.optimal.tracker"

    defaultConfig {
        applicationId = "dev.optimal.tracker"
        versionCode = 8
        versionName = "0.0.1"
    }

    buildTypes {
        debug {
            applicationIdSuffix = OptimalBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = true

            applicationIdSuffix = OptimalBuildType.RELEASE.applicationIdSuffix

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.tracing.ktx)

    ksp(libs.hilt.compiler)

    debugImplementation(libs.androidx.compose.ui.testManifest)

    kspTest(libs.hilt.compiler)

    testImplementation(libs.kotlin.test)

    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.kotlin.test)

}

