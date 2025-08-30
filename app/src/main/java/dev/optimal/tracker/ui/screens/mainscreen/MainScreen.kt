package dev.optimal.tracker.ui.screens.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.optimal.tracker.ui.component.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    // Observe the UI state from the ViewModel
    val uiState by mainViewModel.uiState.collectAsState()

    // Load initial data when the app starts
//    LaunchedEffect(Unit) {
//        mainViewModel.loadUserData()
//    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                title = {
                    Text(
                        text = getScreenTitle(navController),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            // Only show FAB on certain screens
            if (getCurrentRoute(navController) == Screen.Workouts.route) {
                FloatingActionButton(
                    onClick = { /* Add new workout */ },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Filled.Search, contentDescription = "Add workout")
                }
            }
        },
        bottomBar = {
            OptimalBottomNavigation(navController = navController)
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            Navigation(navController)

            if (uiState.isLoading) {
                LoadingIndicator(Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
            }
        }
    }
}

/**
 * Bottom navigation component with tabs for Home, Workouts, and Profile
 */
@Composable
fun OptimalBottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem(Screen.Home.route, "Home", Icons.Default.Home),
        BottomNavItem(Screen.Workouts.route, "Workouts", Icons.Default.CalendarToday),
        BottomNavItem(Screen.Profile.route, "Profile", Icons.Default.Person)
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

/**
 * Navigation host that contains all the screens in the app
 */
@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Workouts.route) {
            WorkoutsScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
        composable(Screen.WorkoutDetail.route + "/{workoutId}") { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId")
            WorkoutDetailScreen(workoutId)
        }
        composable(Screen.ExerciseDetail.route + "/{exerciseId}") { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId")
            ExerciseDetailScreen(exerciseId)
        }
    }
}

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Home Screen - Dashboard with progress charts")
    }
}

@Composable
fun WorkoutsScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Workouts Screen - List of workout plans")
    }
}

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Profile Screen - User settings and stats")
    }
}

@Composable
fun WorkoutDetailScreen(workoutId: String?) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Workout Detail for ID: $workoutId")
    }
}

@Composable
fun ExerciseDetailScreen(exerciseId: String?) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Exercise Detail for ID: $exerciseId")
    }
}

@Composable
fun getScreenTitle(navController: NavController): String {
    return when (getCurrentRoute(navController)) {
        Screen.Home.route -> "Optimal"
        Screen.Workouts.route -> "My Workouts"
        Screen.Profile.route -> "Profile"
        Screen.WorkoutDetail.route -> "Workout Details"
        Screen.ExerciseDetail.route -> "Exercise Details"
        else -> "Optimal"
    }
}

@Composable
fun getCurrentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Workouts : Screen("workouts")
    object Profile : Screen("profile")
    object WorkoutDetail : Screen("workout_detail")
    object ExerciseDetail : Screen("exercise_detail")
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}