package dev.optimal.tracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.optimal.tracker.core.ui.components.OptimalNavigationBar
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
                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigate(screen.route) },
                    icon = {
                        Icon(
                            imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                            contentDescription = stringResource(screen.iconTextId),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text(stringResource(screen.iconTextId)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.tertiary,
                        selectedTextColor = MaterialTheme.colorScheme.tertiary,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
