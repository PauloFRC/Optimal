package dev.optimal.tracker.navigation

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import dev.optimal.tracker.home.navigation.homeNavGraph
import dev.optimal.tracker.navigation.transition.LocalSharedTransitionScope
import dev.optimal.tracker.navigation.transition.OptimalTransition
import dev.optimal.tracker.profile.navigation.profileNavGraph
import dev.optimal.tracker.ui.OptimalAppState
import dev.optimal.tracker.workout.navigation.workoutNavGraph

@Composable
fun AppNavHost(
    appState: OptimalAppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    val topLevelRoutes = topLevelDestinations.map { it.route }

    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this@SharedTransitionLayout
        ) {
            NavHost(
                navController = navController,
                startDestination = TopLevelRoute.Home,
                modifier = modifier,
                enterTransition = {
                    val direction = topLevelRoutes.slideDirection(
                        from = initialState.destination,
                        to = targetState.destination,
                    )
                    if (direction != null) {
                        OptimalTransition.TopLevel(direction).enter(this)
                    } else {
                        OptimalTransition.Default.enter(this)
                    }
                },
                exitTransition = {
                    val direction = topLevelRoutes.slideDirection(
                        from = initialState.destination,
                        to = targetState.destination,
                    )
                    if (direction != null) {
                        OptimalTransition.TopLevel(direction).exit(this)
                    } else {
                        OptimalTransition.Default.exit(this)
                    }
                },
                popEnterTransition = OptimalTransition.Default.popEnter,
                popExitTransition = OptimalTransition.Default.popExit,
            ) {
                homeNavGraph(
                    onNavigateToSessionDetail = { sessionId ->
                        appState.navigateToDetail(DetailRoute.WorkoutSession(sessionId))
                    },
                    onNavigateBack = appState::tryPopBack
                )

                workoutNavGraph(
                    onNavigateToCreateTemplate = {
                        appState.navigateToDetail(DetailRoute.WorkoutTemplateCreation)
                    },
                    onNavigateToDetail = {
                        //appState.navigateToTopLevel(TopLevelRoute.Workout)
                    },
                    onNavigateBack = appState::tryPopBack,
                    onNavigateToExerciseSelection = {
                        appState.navigateToDetail(DetailRoute.ExerciseSelection)
                    },
                    onSelectExercise = { exerciseId ->
                        appState.navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selected_exercise_id", exerciseId)
                        appState.tryPopBack()
                    }
                )

                profileNavGraph(
                    onNavigateToWorkout = {
                        appState.navigateToTopLevel(TopLevelRoute.Workout)
                    }
                )
            }
        }
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
