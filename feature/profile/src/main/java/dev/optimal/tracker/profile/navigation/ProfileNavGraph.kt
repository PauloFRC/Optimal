package dev.optimal.tracker.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.optimal.tracker.navigation.TopLevelRoute
import dev.optimal.tracker.profile.ProfileScreenRoute

fun NavGraphBuilder.profileNavGraph(
    onNavigateToWorkout: () -> Unit,
) {
    composable<TopLevelRoute.Profile> {
        ProfileScreenRoute(
            onStartWorkoutClick = onNavigateToWorkout
        )
    }
}
