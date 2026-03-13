package dev.optimal.tracker.designsystem.theme

import androidx.annotation.VisibleForTesting
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@VisibleForTesting
val OptimalDarkColorScheme = darkColorScheme(
    primary = Titanium,
    onPrimary = Charcoal,
    background = Charcoal,
    onBackground = Mist,
    surface = LighterCharcoal,
    onSurface = Mist,
    surfaceVariant = Charcoal,
    onSurfaceVariant = Titanium,
    outline = Iron,
    outlineVariant = Dim,
    error = Danger,
    onError = Charcoal,
    secondary = Slate,
    onSecondary = Charcoal,
    tertiary = Success,
    onTertiary = Charcoal
)

@Composable
fun OptimalTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = OptimalDarkColorScheme,
        typography  = OptimalTypography,
        content     = content,
    )
}