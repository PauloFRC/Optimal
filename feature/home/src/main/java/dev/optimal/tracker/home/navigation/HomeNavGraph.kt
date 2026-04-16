package dev.optimal.tracker.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.optimal.tracker.home.HomeScreenRoute
import dev.optimal.tracker.home.detail.WorkoutSessionDetailScreenRoute
import dev.optimal.tracker.navigation.DetailRoute
import dev.optimal.tracker.navigation.TopLevelRoute

fun NavGraphBuilder.homeNavGraph(
    onNavigateToSessionDetail: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    composable<TopLevelRoute.Home> {
        HomeScreenRoute(
            onSessionClick = onNavigateToSessionDetail
        )
    }

    composable<DetailRoute.WorkoutSession> {
        WorkoutSessionDetailScreenRoute()
    }
}
