package dev.optimal.tracker.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.optimal.tracker.designsystem.theme.OptimalTheme

@Composable
fun AffirmativeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(3.dp))
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(3.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                onClick = onClick
            ),
//            .debouncedClicks(onClick = onClick,),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun AffirmativeButtonPreview() {
    OptimalTheme {
        AffirmativeButton(text = "Confirm", onClick = {})
    }
}
