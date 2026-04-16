package dev.optimal.tracker.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import dev.optimal.tracker.home.navigation.homeNavGraph
import dev.optimal.tracker.profile.navigation.profileNavGraph
import dev.optimal.tracker.ui.OptimalAppState
import dev.optimal.tracker.workout.navigation.workoutNavGraph

private const val NAV_ANIM_DURATION_MS = 150

@Composable
fun AppNavHost(
    appState: OptimalAppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    val topLevelRoutes = topLevelDestinations.map { it.route }

    NavHost(
        navController = navController,
        startDestination = TopLevelRoute.Home,
        modifier = modifier,
        enterTransition = {
            val direction = topLevelRoutes.slideDirection(
                from = initialState.destination,
                to = targetState.destination,
            )
            slideInHorizontally(
                animationSpec = tween(NAV_ANIM_DURATION_MS),
                initialOffsetX = { if ((direction ?: 1) > 0) it else -it },
            )
        },
        exitTransition = {
            val direction = topLevelRoutes.slideDirection(
                from = initialState.destination,
                to = targetState.destination,
            )
            slideOutHorizontally(
                animationSpec = tween(NAV_ANIM_DURATION_MS),
                targetOffsetX = { if ((direction ?: 1) > 0) -it else it },
            )
        },
        popEnterTransition = {
            slideInHorizontally(animationSpec = tween(NAV_ANIM_DURATION_MS)) { -it }
        },
        popExitTransition = {
            slideOutHorizontally(animationSpec = tween(NAV_ANIM_DURATION_MS)) { it }
        },
    ) {
        homeNavGraph(
            onNavigateToSessionDetail = { sessionId ->
                appState.navigateToDetail(DetailRoute.WorkoutSession(sessionId))
            },
            onNavigateBack = appState::tryPopBack
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

private fun List<TopLevelRoute>.slideDirection(
    from: androidx.navigation.NavDestination?,
    to:   androidx.navigation.NavDestination?,
): Int? {
    val fromIndex = indexOf(from.toTopLevel())
    val toIndex   = indexOf(to.toTopLevel())
    return if (fromIndex >= 0 && toIndex >= 0) toIndex - fromIndex else null
}
