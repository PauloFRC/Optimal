package dev.optimal.tracker.designsystem.theme

import androidx.annotation.VisibleForTesting
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@VisibleForTesting
val darkTheme = darkColorScheme(
    primary            = BlazeOrange,
    onPrimary          = OnPrimary,
    primaryContainer   = BlazeOrangeDark,
    onPrimaryContainer = BlazeOrangeLight,
    secondary            = ElectricCyan,
    onSecondary          = OnSecondary,
    secondaryContainer   = ElectricCyanDark,
    onSecondaryContainer = ElectricCyanLight,
    tertiary            = PowerLime,
    onTertiary          = Color(0xFF1B3700), //TODO: use Color
    tertiaryContainer   = PowerLimeDark,
    onTertiaryContainer = Color(0xFF0D2000),
    error            = Error,
    onError          = OnError,
    errorContainer   = Color(0xFF93000A), //TODO: use Color
    onErrorContainer = Color(0xFFFFDAD6), //TODO: use Color
    background         = BackgroundDark,
    onBackground       = OnBackground,
    surface            = SurfaceDark,
    onSurface          = OnSurface,
    surfaceVariant     = SurfaceVariant,
    onSurfaceVariant   = OnSurfaceVariant,
    surfaceContainer   = SurfaceContainer,
    outline        = OutlineDark,
    outlineVariant = OutlineVariant,
    scrim = Color(0x99000000),
)

@Composable
fun OptimalTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = darkTheme,
        typography  = Typography,   // wire up your Type.kt here
        content     = content,
    )
}