package dev.optimal.tracker.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.optimal.tracker.core.designsystem.R
import dev.optimal.tracker.core.model.DropdownAction
import dev.optimal.tracker.core.model.IconAction
import dev.optimal.tracker.core.model.TextAction
import dev.optimal.tracker.core.model.TopBarAction
import dev.optimal.tracker.core.utils.debounced
import dev.optimal.tracker.core.utils.debouncedClicks

private const val MIN_ICON_SIZE_DP = 16

@Composable
fun RowScope.TopBarActionRow(
    actions: List<TopBarAction>,
    customRenderer: (@Composable (TopBarAction) -> Boolean)? = null
) {
    val visibleActions = actions.filter { it.isVisible && it !is DropdownAction }
    val dropdownActions: List<DropdownAction> = actions.filter { it.isVisible }.filterIsInstance<DropdownAction>()

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        visibleActions.forEach { action ->
            val rendered = customRenderer?.invoke(action) ?: false
            if (!rendered) {
                when (action) {
                    is IconAction -> {
                        IconButton(onClick = action.onClick.debounced()) {
                            Icon(
                                painter = painterResource(action.iconRes),
                                contentDescription = stringResource(id = action.titleRes),
                                tint = action.tint ?: MaterialTheme.colorScheme.primary,
                                modifier = Modifier.defaultMinSize(
                                    minWidth = MIN_ICON_SIZE_DP.dp,
                                    minHeight = MIN_ICON_SIZE_DP.dp
                                )
                            )
                        }
                    }

                    is TextAction -> {
                        Text(
                            text = stringResource(action.titleRes).uppercase(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = when {
                                !action.isEnabled -> action.textDisabledColor
                                    ?: MaterialTheme.colorScheme.outlineVariant
                                else -> action.textColor
                                    ?: MaterialTheme.colorScheme.primary
                            },
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .debouncedClicks(enabled = action.isEnabled) {
                                    action.onClick()
                                }
                        )
                    }
                }
            }
        }

        if (dropdownActions.isNotEmpty()) {
            DropdownMenuButton(dropdownActions)
        }
    }
}

@Composable
fun DropdownMenuButton(actions: List<DropdownAction>) {
    if (actions.isNotEmpty()) {
        var expanded by remember { mutableStateOf(false) }
        Box {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(
                        R.string.core_designsystem_dropdown_description
                    ),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                actions.forEach { action ->
                    DropdownMenuItem(
                        text = { Text(stringResource(id = action.titleRes)) },
                        onClick = {
                            action.onClick()
                            expanded = false
                        }.debounced()
                    )
                }
            }
        }
    }
}
