package dev.optimal.tracker.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.optimal.tracker.core.ui.components.AffirmativeButton
import dev.optimal.tracker.core.ui.components.DarkButton
import dev.optimal.tracker.core.ui.components.NavigationIcon
import dev.optimal.tracker.core.ui.components.OptimalTopAppBar
import dev.optimal.tracker.designsystem.theme.OptimalTheme
import dev.optimal.tracker.feature.workout.R
import dev.optimal.tracker.model.workout.TemplateExerciseModel
import dev.optimal.tracker.model.workout.TemplateSetModel
import dev.optimal.tracker.model.workout.WorkoutTemplateModel
import dev.optimal.tracker.model.workout.enums.SetType
import dev.optimal.tracker.workout.components.WorkoutTemplateCard

@Composable
fun WorkoutScreenRoute(
    onNavigateToCreateTemplate: () -> Unit,
    onNavigateToDetail: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: WorkoutViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        OptimalTopAppBar(
            title = stringResource(R.string.feature_workout_title),
            navigationIcon = NavigationIcon.None
        )

        WorkoutScreen(
            uiState = uiState.value,
            onNavigateToCreateTemplate = onNavigateToCreateTemplate,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateBack = onNavigateBack
        )
    }
}

@Composable
fun WorkoutScreen(
    uiState: WorkoutState,
    onNavigateToCreateTemplate: () -> Unit,
    onNavigateToDetail: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        AffirmativeButton(
            text = stringResource(R.string.feature_workout_new_routine),
            onClick = onNavigateToCreateTemplate,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
        DarkButton(
            text = stringResource(R.string.feature_workout_start_empty_workout),
            onClick = onNavigateToDetail,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.feature_workout_templates_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(12.dp))

        uiState.workoutTemplates.forEach {
            WorkoutTemplateCard(
                workoutTemplate = it,
                onClick = onNavigateToDetail
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview
@Composable
fun WorkoutScreenPreview() {
    OptimalTheme {
        WorkoutScreen(
            uiState = WorkoutState(
                workoutTemplates = listOf(
                    WorkoutTemplateModel(
                        id = 1,
                        name = "Test Workout",
                        exercises = listOf(
                            TemplateExerciseModel(
                                id = 1,
                                name = "Bench Press",
                                sets = listOf(
                                    TemplateSetModel(1, 1, SetType.WORKING),
                                    TemplateSetModel(2, 2, SetType.WORKING),
                                    TemplateSetModel(3, 3, SetType.WORKING),
                                )
                            ),
                            TemplateExerciseModel(
                                id = 2,
                                name = "Squat",
                                sets = listOf(
                                    TemplateSetModel(1, 1, SetType.WORKING),
                                    TemplateSetModel(2, 2, SetType.WORKING),
                                    TemplateSetModel(3, 3, SetType.WORKING),
                                )
                            )
                        )
                    ),
                    WorkoutTemplateModel(
                        id = 2,
                        name = "Test Workout 2",
                        exercises = listOf(
                            TemplateExerciseModel(
                                id = 1,
                                name = "Bench Press",
                                sets = listOf(
                                    TemplateSetModel(1, 1, SetType.WORKING),
                                    TemplateSetModel(2, 2, SetType.WORKING),
                                    TemplateSetModel(3, 3, SetType.WORKING),
                                )
                            ),
                            TemplateExerciseModel(
                                id = 2,
                                name = "Squat",
                                sets = listOf(
                                    TemplateSetModel(1, 1, SetType.WORKING),
                                    TemplateSetModel(2, 2, SetType.WORKING),
                                    TemplateSetModel(3, 3, SetType.WORKING),
                                )
                            )
                        )
                    )
                )
            ),
            onNavigateToCreateTemplate = {},
            onNavigateToDetail = {},
            onNavigateBack = {}
        )
    }
}