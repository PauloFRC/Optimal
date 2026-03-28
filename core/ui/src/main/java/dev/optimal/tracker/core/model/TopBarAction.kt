package dev.optimal.tracker.core.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

interface TopBarAction {
    val onClick: () -> Unit
    val isVisible: Boolean
}

data class IconAction(
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int,
    override val onClick: () -> Unit,
    override val isVisible: Boolean = true,
    val tint: Color? = null
) : TopBarAction

data class DropdownAction(
    @StringRes val titleRes: Int,
    override val onClick: () -> Unit,
    override val isVisible: Boolean = true,
) : TopBarAction

data class TextAction(
    @StringRes val titleRes: Int,
    val textStyle: TextStyle? = null,
    val textColor: Color? = null,
    val textDisabledColor: Color? = null,
    val isEnabled: Boolean = true,
    override val onClick: () -> Unit,
    override val isVisible: Boolean = true,
) : TopBarAction
