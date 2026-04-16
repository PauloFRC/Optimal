package dev.optimal.tracker.navigation

import kotlinx.serialization.Serializable

sealed interface AppRoute

@Serializable
sealed interface TopLevelRoute : AppRoute {
    @Serializable
    data object Home : TopLevelRoute
    @Serializable
    data object Workout : TopLevelRoute
    @Serializable
    data object Profile : TopLevelRoute
}

@Serializable
sealed interface DetailRoute : AppRoute {
    @Serializable
    data class WorkoutSession(val sessionId: Long) : DetailRoute
}
