package dev.optimal.tracker.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.optimal.tracker.R

data class TopLevelDestination<out T : Any>(
    val route: T,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
)

val topLevelDestinations = listOf(
    TopLevelDestination(
        route = TopLevelRoute.Home,
        selectedIcon = R.drawable.ic_home_filled,
        unselectedIcon = R.drawable.ic_home_outline,
        iconTextId = R.string.home_icon_description,
        titleTextId = R.string.app_name,
    ),
    TopLevelDestination(
        route = TopLevelRoute.Workout,
        selectedIcon = R.drawable.ic_dumbell_filled,
        unselectedIcon = R.drawable.ic_dumbell_outline,
        iconTextId = R.string.workout_icon_description,
        titleTextId = R.string.app_name,
    ),
    TopLevelDestination(
        route = TopLevelRoute.Profile,
        selectedIcon = R.drawable.ic_profile_filled,
        unselectedIcon = R.drawable.ic_profile_outline,
        iconTextId = R.string.profile_icon_description,
        titleTextId = R.string.app_name,
    )
)
