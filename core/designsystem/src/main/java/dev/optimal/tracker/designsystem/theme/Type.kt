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
        lineHeight = 40.sp,
        color = HighEmphasis
    ),

    headlineMedium = TextStyle(
        fontFamily = SansFlex,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        color = HighEmphasis
    ),


    headlineSmall = TextStyle(
        fontFamily = SansFlex,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = HighEmphasis
    ),

    bodyMedium = TextStyle(
        fontFamily = SansFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = HighEmphasis
    ),

    labelMedium = TextStyle(
        fontFamily = SansFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = MediumEmphasis
    ),

    labelSmall = TextStyle(
        fontFamily = SansFlex,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        color = MediumEmphasis
    )
)
