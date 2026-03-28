package dev.optimal.tracker.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.optimal.tracker.R
import dev.optimal.tracker.core.model.IconAction
import dev.optimal.tracker.core.ui.components.OptimalTopAppBar
import dev.optimal.tracker.core.ui.components.SearchTopAppBar
import dev.optimal.tracker.navigation.AppNavHost
import dev.optimal.tracker.navigation.TopLevelDestination
import dev.optimal.tracker.navigation.TopLevelRoute
import dev.optimal.tracker.navigation.topLevelDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptimalApp() {
    val appState = rememberOptimalAppState()
    val currentDestination = appState.currentTopLevel

    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    BackHandler(enabled = isSearchActive) {
        isSearchActive = false
        searchQuery = ""
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            if (isSearchActive) {
                SearchTopAppBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onCloseClicked = {
                        isSearchActive = false
                        searchQuery = ""
                    }
                )
            } else {
                OptimalTopAppBar(
                    title = currentDestination?.let { stringResource(it.titleTextId) },
                    actions = listOf(
                        IconAction(
                            titleRes = R.string.search,
                            iconRes = R.drawable.ic_search,
                            onClick = { isSearchActive = true }
                        )
                    )
                )
            }
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
                    currentTopLevel = currentDestination,
                    onNavigate = appState::navigateToTopLevel
                )
        )
    }
}

private fun Modifier.swipeToNavigate(
    destinations: List<TopLevelDestination<TopLevelRoute>>,
    currentTopLevel: TopLevelDestination<TopLevelRoute>?,
    onNavigate: (TopLevelRoute) -> Unit,
    thresholdDp: Int = 72,
): Modifier = composed {
    val thresholdPx = with(LocalDensity.current) { thresholdDp.dp.toPx() }
    var accumulated by remember(currentTopLevel) { mutableFloatStateOf(0f) }

    pointerInput(currentTopLevel) {
        detectHorizontalDragGestures(
            onDragCancel = { accumulated = 0f },
            onDragEnd = {
                val index = destinations.indexOf(currentTopLevel)
                when {
                    accumulated < -thresholdPx && index < destinations.lastIndex ->
                        onNavigate(destinations[index + 1].route)
                    accumulated > thresholdPx && index > 0 ->
                        onNavigate(destinations[index - 1].route)
                }
                accumulated = 0f
            },
            onHorizontalDrag = { _, delta -> accumulated += delta }
        )
    }
}
