package dev.optimal.tracker.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.optimal.tracker.core.designsystem.R
import dev.optimal.tracker.core.model.DropdownAction
import dev.optimal.tracker.core.model.TopBarAction
import dev.optimal.tracker.designsystem.theme.OptimalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptimalTopAppBar(
    title: String?,
    modifier: Modifier = Modifier,
    actions: List<TopBarAction>? = null
) {
    Column(
        modifier = modifier
    ) {
        TopAppBar(
            title = { Text(text = title ?: "") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground
            ),
            windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top),
            actions = {
                actions?.let {
                    TopBarActionRow(
                        actions = actions
                    )
                }
            }
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}

@Preview
@Composable
fun SimplePreview() {
    OptimalTheme {
        OptimalTopAppBar(
            "Title",
            actions = listOf(
                DropdownAction(
                    R.string.core_designsystem_dropdown_description,
                    onClick = {}
                )
            )
        )
    }
}
