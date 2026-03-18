package dev.optimal.tracker.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface TopLevelRoute {
    @Serializable
    data object Home : TopLevelRoute
    @Serializable
    data object Workout : TopLevelRoute
    @Serializable
    data object Profile : TopLevelRoute
}
