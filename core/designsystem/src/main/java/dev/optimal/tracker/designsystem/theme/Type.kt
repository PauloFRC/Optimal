package dev.optimal.tracker.designsystem.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun OptimalTypographyPreview() {
    OptimalTheme {
        Surface {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Headline Large (32sp)",
                    style = OptimalTypography.headlineLarge
                )
                Text(
                    text = "Headline Medium (24sp)",
                    style = OptimalTypography.headlineMedium
                )
                Text(
                    text = "Headline Small (16sp)",
                    style = OptimalTypography.headlineSmall
                )
                Text(
                    text = "Body Medium (16sp)",
                    style = OptimalTypography.bodyMedium
                )
                Text(
                    text = "Label Medium (16sp)",
                    style = OptimalTypography.labelMedium
                )
                Text(
                    text = "Label Small (12sp)",
                    style = OptimalTypography.labelSmall
                )
            }
        }
    }
}
