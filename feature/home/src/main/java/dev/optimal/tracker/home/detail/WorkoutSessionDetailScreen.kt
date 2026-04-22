package dev.optimal.tracker.home.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.optimal.tracker.core.ui.components.OptimalTopAppBar
import dev.optimal.tracker.designsystem.theme.Dim
import dev.optimal.tracker.designsystem.theme.Iron
import dev.optimal.tracker.designsystem.theme.OptimalTheme
import dev.optimal.tracker.feature.home.R
import dev.optimal.tracker.model.workout.SessionExerciseModel
import dev.optimal.tracker.model.workout.SessionSetModel
import dev.optimal.tracker.model.workout.WorkoutSessionModel
import dev.optimal.tracker.model.workout.enums.SetType
import dev.optimal.tracker.model.workout.getFormattedDuration
import dev.optimal.tracker.model.workout.getFormattedStartDate
import dev.optimal.tracker.navigation.transition.NAV_ANIM_DURATION_MS
import java.time.LocalDateTime

@Composable
fun WorkoutSessionDetailScreenRoute(
    sessionId: Long,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBackClick: () -> Unit,
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
            WorkoutSessionDetailContentScreen(
                sessionId = sessionId,
                uiState = uiState,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                onBackClick = onBackClick
            )
        }
    }
}

@Composable
fun WorkoutSessionDetailErrorScreen(
    message: String?,
    onRetryClick: () -> Unit
) {
    Scaffold(
        topBar = {
            OptimalTopAppBar(
                title = stringResource(R.string.feature_home_session_detail_title),
                showBackIcon = true,
                onBackClick = {}
            )
        }
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
    sessionId: Long,
    uiState: WorkoutSessionDetailState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBackClick: () -> Unit
) {
    val isLoading = uiState is WorkoutSessionDetailState.Loading
    val session = (uiState as? WorkoutSessionDetailState.Success)?.session

    with(sharedTransitionScope) {
        Column(
            modifier = Modifier
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        key = "session_bounds_$sessionId"
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                    resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                    clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(0.dp)),
                )
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            OptimalTopAppBar(
                title = stringResource(R.string.feature_home_session_detail_title),
                showBackIcon = true,
                onBackClick = onBackClick
            )

            Box(
                modifier = Modifier
//                    .padding(padding)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    ShimmerText(
                        text = session?.name ?: "",
                        isLoading = isLoading,
                        style = MaterialTheme.typography.headlineLarge,
                        shimmerWidth = 300.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        ShimmerText(
                            text = session?.getFormattedStartDate() ?: "",
                            isLoading = isLoading,
                            style = MaterialTheme.typography.headlineSmall,
                            shimmerWidth = 70.dp
                        )
                        Text(
                            " • ",
                            style = MaterialTheme.typography.headlineSmall.copy(color = Iron)
                        )
                        ShimmerText(
                            text = session?.getFormattedDuration() ?: "",
                            isLoading = isLoading,
                            style = MaterialTheme.typography.headlineSmall,
                            shimmerWidth = 70.dp
                        )
                    }

//                val numPRs = session?.getPersonalRecords()?.size
                    val numPRs = 10
                    if (!isLoading && numPRs != null && numPRs > 0) {
                        Text(
                            text = "$numPRs PRS",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .background(
                                    MaterialTheme.colorScheme.inverseSurface,
                                    RectangleShape
                                )
                                .padding(horizontal = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(64.dp))

                    val exercises = session?.exercises
                    exercises?.let {
                        LazyColumn {
                            items(exercises) { exercise ->
                                SessionExerciseDetail(exercise, isLoading)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SessionExerciseDetail(
    exercise: SessionExerciseModel?,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ShimmerText(
            text = exercise?.name,
            isLoading = isLoading,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        ShimmerText(
            text = "muscle group", //TODO: add primary muscle group to model
            isLoading = isLoading,
            style = MaterialTheme.typography.headlineSmall,
            color = Dim
        )
    }

    exercise?.sets?.forEach { set ->
        Row {
            ShimmerText(
                text = "SET ${set.order}",
                isLoading = isLoading,
                style = MaterialTheme.typography.headlineSmall,
                color = Dim,
                modifier = Modifier.padding(end = 16.dp)
            )
            ShimmerText(
                text = "${set.weight} x ${set.reps}",
                isLoading = isLoading,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}

@Composable
fun ShimmerText(
    text: String?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    shimmerWidth: Dp = 100.dp,
) {
    if (isLoading) {
        val brush = rememberShimmerBrush()
        val lineHeight = with(LocalDensity.current) { style.fontSize.toDp() * 1.4f }
        Box(
            modifier = modifier
                .width(shimmerWidth)
                .height(lineHeight)
                .clip(RoundedCornerShape(4.dp))
                .background(brush)
        )
    } else {
        text?.let {
            Text(
                text = it,
                style = style,
                color = color,
                modifier = modifier,
            )
        }
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
            SessionExerciseModel(1, "Bench Press",
                listOf(SessionSetModel(1, 1, SetType.WORKING, true, 10, 100.0, null))
            ),
            SessionExerciseModel(2, "Squat", listOf()),
            SessionExerciseModel(3, "Deadlift", listOf())
        )
    )
    OptimalTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                WorkoutSessionDetailContentScreen(
                    sessionId = 1L,
                    uiState = WorkoutSessionDetailState.Success(session = session),
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    onBackClick = {}
                )
            }
        }
    }
}

@Preview
@Composable
fun WorkoutSessionDetailScreenLoadingPreview() {
    OptimalTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                WorkoutSessionDetailContentScreen(
                    sessionId = 1L,
                    uiState = WorkoutSessionDetailState.Loading,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    onBackClick = {}
                )
            }
        }
    }
}

@Preview
@Composable
fun WorkoutSessionDetailScreenErrorPreview() {
    OptimalTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                WorkoutSessionDetailContentScreen(
                    sessionId = 1L,
                    uiState = WorkoutSessionDetailState.Error("Error"),
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    onBackClick = {}
                )
            }
        }
    }
}
