package dev.optimal.tracker.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val OptimalTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = SansFlex,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = SansFlex,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = SansFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),

    labelMedium = TextStyle(
        fontFamily = SansFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)
