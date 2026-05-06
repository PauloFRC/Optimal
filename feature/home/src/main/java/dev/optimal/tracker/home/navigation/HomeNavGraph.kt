package dev.optimal.tracker.home.navigation

import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.optimal.tracker.home.HomeScreenRoute
import dev.optimal.tracker.home.detail.WorkoutSessionDetailScreenRoute
import dev.optimal.tracker.navigation.DetailRoute
import dev.optimal.tracker.navigation.TopLevelRoute
import dev.optimal.tracker.navigation.transition.LocalNavAnimatedVisibilityScope
import dev.optimal.tracker.navigation.transition.OptimalTransition

fun NavGraphBuilder.homeNavGraph(
    onNavigateToSessionDetail: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    composable<TopLevelRoute.Home>(
        exitTransition = {
            if (targetState.destination.hasRoute(DetailRoute.WorkoutSession::class)) {
                OptimalTransition.SharedElement.exit(this)
            } else null
        },
        popEnterTransition = {
            if (initialState.destination.hasRoute(DetailRoute.WorkoutSession::class)) {
                OptimalTransition.SharedElement.popEnter(this)
            } else null
        }
    ) {
        CompositionLocalProvider(
            LocalNavAnimatedVisibilityScope provides this@composable
        ) {
            HomeScreenRoute(
                onSessionClick = onNavigateToSessionDetail
            )
        }
    }

    composable<DetailRoute.WorkoutSession>(
        enterTransition = OptimalTransition.SharedElement.enter,
        exitTransition = OptimalTransition.SharedElement.exit,
        popEnterTransition = OptimalTransition.SharedElement.popEnter,
        popExitTransition = OptimalTransition.SharedElement.popExit
    ) { backStackEntry ->
        CompositionLocalProvider(
            LocalNavAnimatedVisibilityScope provides this@composable
        ) {
            val route = backStackEntry.toRoute<DetailRoute.WorkoutSession>()
            WorkoutSessionDetailScreenRoute(
                sessionId = route.sessionId,
                onBackClick = onNavigateBack
            )
        }
    }
}
