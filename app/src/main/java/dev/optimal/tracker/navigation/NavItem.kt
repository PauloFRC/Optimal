package dev.optimal.tracker.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SportsGymnastics
import androidx.compose.ui.graphics.vector.ImageVector
import dev.optimal.tracker.R

data class NavItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
)

val HOME = NavItem(
    selectedIcon = Icons.Filled.Home,
    unselectedIcon = Icons.Outlined.Home,
    iconTextId = R.string.home_icon_description,
    titleTextId = R.string.app_name,
)

val WORKOUT = NavItem(
    selectedIcon = Icons.Filled.SportsGymnastics,
    unselectedIcon = Icons.Outlined.SportsGymnastics,
    iconTextId = R.string.workout_icon_description,
    titleTextId = R.string.app_name,
)

val PROFILE = NavItem(
    selectedIcon = Icons.Filled.Person,
    unselectedIcon = Icons.Outlined.Person,
    iconTextId = R.string.profile_icon_description,
    titleTextId = R.string.app_name,
)
