package dev.optimal.tracker.home.detail

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.optimal.tracker.core.ui.components.OptimalTopAppBar
import dev.optimal.tracker.designsystem.theme.Iron
import dev.optimal.tracker.designsystem.theme.OptimalTheme
import dev.optimal.tracker.model.workout.SessionExerciseModel
import dev.optimal.tracker.model.workout.WorkoutSessionModel
import dev.optimal.tracker.model.workout.getFormattedDuration
import dev.optimal.tracker.model.workout.getFormattedStartDate
import java.time.LocalDateTime

@Composable
fun WorkoutSessionDetailScreenRoute(
    viewModel: WorkoutSessionDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is WorkoutSessionDetailState.Error -> {
            WorkoutSessionDetailErrorScreen(
                message = (uiState as WorkoutSessionDetailState.Error).message,
                onRetryClick = {}
            )
        }
        else -> {
            WorkoutSessionDetailContentScreen(uiState = uiState)
        }
    }
}

@Composable
fun WorkoutSessionDetailErrorScreen(
    message: String?,
    onRetryClick: () -> Unit
) {
    Scaffold(
        topBar = { OptimalTopAppBar(title = "") }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text("ERROOO!!!!!!!", style = MaterialTheme.typography.headlineLarge)
        }
    }
}

@Composable
fun WorkoutSessionDetailContentScreen(
    uiState: WorkoutSessionDetailState
) {
    val isLoading = uiState is WorkoutSessionDetailState.Loading
    val session = (uiState as? WorkoutSessionDetailState.Success)?.session

    Scaffold(
        topBar = { OptimalTopAppBar(title = "") }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                ShimmerText(
                    text = session?.name ?: "",
                    isLoading = isLoading,
                    style = MaterialTheme.typography.headlineLarge,
                    shimmerWidth = 0.8f
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    ShimmerText(
                        text = session?.getFormattedStartDate() ?: "",
                        isLoading = isLoading,
                        style = MaterialTheme.typography.headlineSmall,
                        shimmerWidth = 0.3f
                    )
                    Text(" • ", style = MaterialTheme.typography.headlineSmall.copy(color = Iron))
                    ShimmerText(
                        text = session?.getFormattedDuration() ?: "",
                        isLoading = isLoading,
                        style = MaterialTheme.typography.headlineSmall,
                        shimmerWidth = 0.3f
                    )
                }
            }
        }
    }
}

@Composable
fun ShimmerText(
    text: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    shimmerWidth: Float = 1f, // fraction of max width
) {
    if (isLoading) {
        val brush = rememberShimmerBrush()
        val lineHeight = with(LocalDensity.current) { style.fontSize.toDp() * 1.4f }
        Box(
            modifier = modifier
                .fillMaxWidth(shimmerWidth)
                .height(lineHeight)
                .clip(RoundedCornerShape(4.dp))
                .background(brush)
        )
    } else {
        Text(
            text = text,
            style = style,
            modifier = modifier,
        )
    }
}

@Composable
fun rememberShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )
    return Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
        ),
        start = Offset.Zero,
        end = Offset(translateAnim, translateAnim)
    )
}

@Preview
@Composable
fun WorkoutSessionDetailScreenPreview() {
    val session = WorkoutSessionModel(
        id = 1,
        workoutModelId = 1,
        name = "Session Name",
        isCompleted = true,
        startDate = LocalDateTime.of(2026, 3, 19, 14, 30),
        endDate = LocalDateTime.of(2026, 3, 19, 17, 15),
        exercises = listOf(
            SessionExerciseModel(1, "Bench Press", listOf()),
            SessionExerciseModel(2, "Squat", listOf()),
            SessionExerciseModel(3, "Deadlift", listOf())
        )
    )
    OptimalTheme {
        WorkoutSessionDetailContentScreen(uiState = WorkoutSessionDetailState.Success(session = session))
    }
}

@Preview
@Composable
fun WorkoutSessionDetailScreenLoadingPreview() {
    OptimalTheme {
        WorkoutSessionDetailContentScreen(uiState = WorkoutSessionDetailState.Loading)
    }
}

@Preview
@Composable
fun WorkoutSessionDetailScreenErrorPreview() {
    OptimalTheme {
        WorkoutSessionDetailContentScreen(uiState = WorkoutSessionDetailState.Error(message = "Error"))
    }
}