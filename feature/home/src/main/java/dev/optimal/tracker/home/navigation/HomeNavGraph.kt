package dev.optimal.tracker.home.navigation

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.optimal.tracker.home.HomeScreenRoute
import dev.optimal.tracker.home.detail.WorkoutSessionDetailScreenRoute
import dev.optimal.tracker.navigation.DetailRoute
import dev.optimal.tracker.navigation.TopLevelRoute
import dev.optimal.tracker.navigation.transition.OptimalTransition

fun NavGraphBuilder.homeNavGraph(
    sharedTransitionScope: SharedTransitionScope,
    onNavigateToSessionDetail: (Long) -> Unit,
    onNavigateBack: () -> Unit
) {
    composable<TopLevelRoute.Home>(
        exitTransition = {
            if (targetState.destination.hasRoute<DetailRoute.WorkoutSession>()) {
                OptimalTransition.SharedElement.exit(this)
            } else {
                OptimalTransition.Default.exit(this)
            }
        },
        popEnterTransition = {
            if (initialState.destination.hasRoute<DetailRoute.WorkoutSession>()) {
                OptimalTransition.SharedElement.popEnter.invoke(this)
            } else {
                OptimalTransition.Default.popEnter.invoke(this)
            }
        }
    ) {
        HomeScreenRoute(
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = this,
            onSessionClick = onNavigateToSessionDetail
        )
    }

    composable<DetailRoute.WorkoutSession>(
        enterTransition = OptimalTransition.SharedElement.enter,
        exitTransition = OptimalTransition.SharedElement.exit,
        popEnterTransition = OptimalTransition.SharedElement.popEnter,
        popExitTransition = OptimalTransition.SharedElement.popExit,
    ) { backStackEntry ->
        val route = backStackEntry.toRoute<DetailRoute.WorkoutSession>()
        WorkoutSessionDetailScreenRoute(
            sessionId = route.sessionId,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = this,
            onBackClick = onNavigateBack
        )
    }
}
