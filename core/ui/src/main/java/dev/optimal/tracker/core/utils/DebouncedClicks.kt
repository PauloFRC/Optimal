package dev.optimal.tracker.core.utils

import android.os.SystemClock
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

fun Modifier.debouncedClicks(
    enabled: Boolean = true,
    debounceTime: Duration = 300.milliseconds,
    onClick: () -> Unit
): Modifier {
    return this.composed {
        val clickable = debounced(debounceTime = debounceTime, onClick = onClick)
        val interactionSource = remember { MutableInteractionSource() }
        val indication = LocalIndication.current
        this.clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = indication,               // 👈 pass it explicitly
        ) { clickable() }
    }
}

@Composable
fun (() -> Unit).debounced(debounceTime: Duration = 300.milliseconds): () -> Unit {
    return debounced(debounceTime = debounceTime, onClick = this)
}

@Composable
private inline fun debounced(debounceTime: Duration, crossinline onClick: () -> Unit): () -> Unit {
    var lastTimeClicked by remember { mutableLongStateOf(0L) }
    val onClickLambda: () -> Unit = {
        val now = SystemClock.uptimeMillis()
        if (now - lastTimeClicked > debounceTime.inWholeMilliseconds) {
            onClick()
            lastTimeClicked = now
        }
    }
    return onClickLambda
}
