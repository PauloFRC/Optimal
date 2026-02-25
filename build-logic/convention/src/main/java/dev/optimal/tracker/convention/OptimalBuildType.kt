package dev.optimal.tracker.convention

enum class OptimalBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}