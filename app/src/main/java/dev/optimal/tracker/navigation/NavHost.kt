package dev.optimal.tracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import dev.optimal.tracker.home.navigation.homeNavGraph
import dev.optimal.tracker.profile.navigation.profileNavGraph
import dev.optimal.tracker.ui.OptimalAppState
import dev.optimal.tracker.workout.navigation.workoutNavGraph

@Composable
fun AppNavHost(
    appState: OptimalAppState,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = TopLevelRoute.Home,
        modifier = modifier
    ) {
        homeNavGraph(
            onNavigateToWorkout = {
                appState.navigateToTopLevel(TopLevelRoute.Workout)
            }
        )

        workoutNavGraph(
            onNavigateToSession = {
                //appState.navigateToTopLevel(TopLevelRoute.Workout)
            },
            onNavigateToDetail = {
                //appState.navigateToTopLevel(TopLevelRoute.Workout)
            },
            onNavigateBack = appState::tryPopBack
        )

        profileNavGraph(
            onNavigateToWorkout = {
                appState.navigateToTopLevel(TopLevelRoute.Workout)
            }
        )
    }
}
