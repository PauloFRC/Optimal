package dev.optimal.tracker.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy

fun NavDestination?.toTopLevel(): TopLevelRoute? = when {
    this == null -> null
    hierarchy.any { it.hasRoute(TopLevelRoute.Home::class) } -> TopLevelRoute.Home
    hierarchy.any { it.hasRoute(TopLevelRoute.Workout::class) } -> TopLevelRoute.Workout
    hierarchy.any { it.hasRoute(TopLevelRoute.Profile::class) } -> TopLevelRoute.Profile
    else -> null
}
