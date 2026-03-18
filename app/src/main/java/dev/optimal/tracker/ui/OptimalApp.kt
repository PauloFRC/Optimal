package dev.optimal.tracker.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.optimal.tracker.navigation.AppNavHost
import dev.optimal.tracker.navigation.topLevelDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptimalApp() {
    val appState = rememberOptimalAppState()
    val currentTopLevel = appState.currentTopLevel
    val isTopLevel = appState.isTopLevelDestination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            if (isTopLevel && currentTopLevel != null) {
                TopAppBar(
                    title = { Text(stringResource(currentTopLevel.titleTextId)) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                )
            }
        },
        bottomBar = {
            if (isTopLevel) {
                NavigationBar(
                    windowInsets = WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                    )
                ) {
                    topLevelDestinations.forEach { screen ->
                        val selected = currentTopLevel?.route == screen.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = { appState.navigateToTopLevel(screen.route) },
                            icon = {
                                Icon(
                                    imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                                    contentDescription = stringResource(screen.iconTextId)
                                )
                            },
                            label = { Text(stringResource(screen.iconTextId)) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(
            appState = appState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
                )
        )
    }
}
