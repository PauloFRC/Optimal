package dev.optimal.tracker.ui

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.optimal.tracker.core.ui.components.OptimalTopAppBar
import dev.optimal.tracker.navigation.AppNavHost
import dev.optimal.tracker.navigation.TopLevelDestination
import dev.optimal.tracker.navigation.TopLevelRoute
import dev.optimal.tracker.navigation.topLevelDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptimalApp() {
    val appState = rememberOptimalAppState()
    val currentDestination = appState.currentTopLevel

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            OptimalTopAppBar(
                title = currentDestination?.let { stringResource(it.titleTextId) }
            )
        },
        bottomBar = {
            OptimalBottomAppBar(
                topLevelDestinations = topLevelDestinations,
                currentTopLevel = currentDestination,
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
                .swipeToNavigate(
                    destinations = topLevelDestinations,
                    current = currentDestination,
                    onNavigate = { appState.navigateToTopLevel(it.route) }
                )
        )
    }
}

private fun Modifier.swipeToNavigate(
    destinations: List<TopLevelDestination<TopLevelRoute>>,
    current: TopLevelDestination<TopLevelRoute>?,
    onNavigate: (TopLevelDestination<TopLevelRoute>) -> Unit,
    thresholdDp: Float = 72f,
): Modifier = composed {
    val thresholdPx = with(LocalDensity.current) { thresholdDp.dp.toPx() }
    var accumulated by remember(current) { mutableFloatStateOf(0f) }

    pointerInput(current) {
        detectHorizontalDragGestures(
            onDragCancel = { accumulated = 0f },
            onDragEnd = {
                val index = destinations.indexOf(current)
                when {
                    accumulated < -thresholdPx && index < destinations.lastIndex ->
                        onNavigate(destinations[index + 1])
                    accumulated > thresholdPx && index > 0 ->
                        onNavigate(destinations[index - 1])
                }
                accumulated = 0f
            },
            onHorizontalDrag = { _, delta -> accumulated += delta }
        )
    }
}
