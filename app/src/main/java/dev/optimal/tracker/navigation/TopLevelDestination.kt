package dev.optimal.tracker.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.optimal.tracker.R
import dev.optimal.tracker.feature.home.R as HomeR
import dev.optimal.tracker.feature.profile.R as ProfileR
import dev.optimal.tracker.feature.workout.R as WorkoutR

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
        iconTextId = HomeR.string.feature_home_title,
        titleTextId = R.string.app_name,
    ),
    TopLevelDestination(
        route = TopLevelRoute.Workout,
        selectedIcon = R.drawable.ic_dumbell_filled,
        unselectedIcon = R.drawable.ic_dumbell_outline,
        iconTextId = WorkoutR.string.feature_workout_title,
        titleTextId = R.string.app_name,
    ),
    TopLevelDestination(
        route = TopLevelRoute.Profile,
        selectedIcon = R.drawable.ic_profile_filled,
        unselectedIcon = R.drawable.ic_profile_outline,
        iconTextId = ProfileR.string.feature_profile_title,
        titleTextId = R.string.app_name,
    )
)
