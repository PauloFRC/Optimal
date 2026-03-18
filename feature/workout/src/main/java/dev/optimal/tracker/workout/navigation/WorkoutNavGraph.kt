package dev.optimal.tracker.workout.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.optimal.tracker.navigation.TopLevelRoute
import dev.optimal.tracker.workout.WorkoutScreenRoute

fun NavGraphBuilder.workoutNavGraph(
    onNavigateToSession: () -> Unit,
    onNavigateToDetail: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    composable<TopLevelRoute.Workout> {
        WorkoutScreenRoute(
            onNavigateToSession = onNavigateToSession,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateBack = onNavigateBack
        )
    }
}
