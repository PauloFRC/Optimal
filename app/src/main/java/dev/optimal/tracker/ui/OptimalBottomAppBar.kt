package dev.optimal.tracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.optimal.tracker.core.ui.components.OptimalNavigationBar
import dev.optimal.tracker.core.ui.components.OptimalNavigationBarItem
import dev.optimal.tracker.designsystem.theme.OptimalTheme
import dev.optimal.tracker.navigation.TopLevelDestination
import dev.optimal.tracker.navigation.TopLevelRoute
import dev.optimal.tracker.navigation.topLevelDestinations

@Composable
fun OptimalBottomAppBar(
    topLevelDestinations: List<TopLevelDestination<TopLevelRoute>>,
    currentTopLevel: TopLevelDestination<TopLevelRoute>?,
    onNavigate: (TopLevelRoute) -> Unit,
) {
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
    ){
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        OptimalNavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            topLevelDestinations.forEach { screen ->
                val selected = currentTopLevel?.route == screen.route
                OptimalNavigationBarItem(
                    selected = selected,
                    onClick = { onNavigate(screen.route) },
                    icon = screen.unselectedIcon,
                    label = stringResource(screen.iconTextId)
                )
            }
        }
    }
}

@Preview
@Composable
fun SimplePreview() {
    OptimalTheme {
        OptimalBottomAppBar(
            topLevelDestinations = topLevelDestinations,
            currentTopLevel = topLevelDestinations.first(),
            onNavigate = {}
        )
    }
}
