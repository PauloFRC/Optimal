package dev.optimal.tracker.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.optimal.tracker.home.HomeScreenRoute
import dev.optimal.tracker.navigation.TopLevelRoute

fun NavGraphBuilder.homeNavGraph(
    onNavigateToWorkout: () -> Unit,
) {
    composable<TopLevelRoute.Home> {
        HomeScreenRoute(
            onStartWorkoutClick = onNavigateToWorkout
        )
    }
}
