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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.optimal.tracker.core.ui.components.OptimalTopAppBar
import dev.optimal.tracker.navigation.AppNavHost
import dev.optimal.tracker.navigation.topLevelDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptimalApp() {
    val appState = rememberOptimalAppState()
    val currentTopLevel = appState.currentTopLevel

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = { OptimalTopAppBar(currentTopLevel?.let { stringResource(it.titleTextId) }) },
        bottomBar = {
            OptimalBottomAppBar(
                topLevelDestinations = topLevelDestinations,
                currentTopLevel = currentTopLevel,
                onNavigate = appState::navigateToTopLevel
            )
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
