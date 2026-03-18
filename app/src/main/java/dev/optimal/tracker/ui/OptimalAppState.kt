package dev.optimal.tracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.optimal.tracker.navigation.TopLevelDestination
import dev.optimal.tracker.navigation.TopLevelRoute
import dev.optimal.tracker.navigation.topLevelDestinations

@Composable
fun rememberOptimalAppState(
    navController: NavHostController = rememberNavController()
): OptimalAppState {
    return remember(navController) {
        OptimalAppState(navController)
    }
}

@Stable
class OptimalAppState(internal val navController: NavHostController) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState()
            .value
            ?.destination

    val currentTopLevel: TopLevelDestination<out TopLevelRoute>?
        @Composable get() {
            val destination = currentDestination ?: return null
            return topLevelDestinations.find { topLevel ->
                destination.hierarchy.any { it.hasRoute(topLevel.route::class) }
            }
        }

    val isTopLevelDestination: Boolean
        @Composable get() = currentTopLevel != null

    fun navigateToTopLevel(topLevelRoute: TopLevelRoute) {
        navController.navigate(topLevelRoute) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun tryPopBack(): Boolean = navController.popBackStack()
}
