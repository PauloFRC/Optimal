package dev.optimal.tracker.home.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.optimal.tracker.core.ui.components.OptimalTopAppBar
import dev.optimal.tracker.core.ui.components.ShimmerText
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

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    ShimmerText(
                        text = session?.name?.uppercase() ?: "",
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

                    Spacer(modifier = Modifier.height(16.dp))

                    val exercises = session?.exercises
                    exercises?.let {
                        LazyColumn {
                            items(exercises) { exercise ->
                                if (exercise.sets.isNotEmpty()) {
                                    SessionExerciseDetail(exercise, isLoading)
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ShimmerText(
            text = exercise?.name?.uppercase(),
            isLoading = isLoading,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        ShimmerText(
            text = "1 RM",
            isLoading = isLoading,
            style = MaterialTheme.typography.headlineSmall,
            color = Dim,
            modifier = Modifier.align(Alignment.Bottom)
        )
    }

    exercise?.sets?.forEach { set ->
        Row {
            ShimmerText(
                text = "SET ${set.order}",
                isLoading = isLoading,
                style = MaterialTheme.typography.labelSmall,
                color = Dim,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 16.dp)
            )
            ShimmerText(
                text = "${set.weight} x ${set.reps}",
                isLoading = isLoading,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.weight(1f))
            ShimmerText(
                // TODO: add calculator for 1 rm
                text = "120 kg",
                isLoading = isLoading,
                style = MaterialTheme.typography.labelMedium,
                color = Dim
            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
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
                listOf(
                    SessionSetModel(1, 1, SetType.WORKING, true, 10, 100.0, null),
                    SessionSetModel(2, 2, SetType.WORKING, true, 10, 100.0, null),
                    SessionSetModel(3, 3, SetType.WORKING, true, 10, 100.0, null)
                )
            ),
            SessionExerciseModel(2, "Squat",
                listOf(
                    SessionSetModel(1, 1, SetType.WORKING, true, 10, 100.0, null),
                    SessionSetModel(2, 2, SetType.WORKING, true, 10, 100.0, null),
                    SessionSetModel(3, 3, SetType.WORKING, true, 10, 100.0, null)
                )
            ),
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
