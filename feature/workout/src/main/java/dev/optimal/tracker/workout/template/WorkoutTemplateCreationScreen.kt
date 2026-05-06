package dev.optimal.tracker.workout.template

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.optimal.tracker.core.model.TextAction
import dev.optimal.tracker.core.ui.components.DarkButton
import dev.optimal.tracker.core.ui.components.NavigationIcon
import dev.optimal.tracker.core.ui.components.OptimalTopAppBar
import dev.optimal.tracker.designsystem.theme.OptimalTheme
import dev.optimal.tracker.feature.workout.R
import dev.optimal.tracker.model.workout.input.WorkoutTemplateInput
import dev.optimal.tracker.core.designsystem.R as CoreR

@Composable
fun WorkoutTemplateCreationScreenRoute(
    onBackClick: () -> Unit,
    onAddExerciseClick: () -> Unit,
    viewModel: WorkoutTemplateCreationViewModel = hiltViewModel(),
    savedStateHandle: SavedStateHandle
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // In case we are popping back from exercise selection
    val selectedExerciseId by savedStateHandle
        .getStateFlow<Long?>("selected_exercise_id", null)
        .collectAsStateWithLifecycle()

    LaunchedEffect(selectedExerciseId) {
        selectedExerciseId?.let { id ->
            viewModel.addExercise(id)
            savedStateHandle.remove<Long>("selected_exercise_id")
        }
    }
    
    WorkoutTemplateCreationScreen(
        workoutTemplate = uiState.workoutTemplate,
        onWorkoutTemplateUpdate = viewModel::onWorkoutTemplateUpdate,
        onSaveClick = viewModel::saveWorkoutTemplate,
        onBackClick = onBackClick,
        onAddExercise = onAddExerciseClick
    )
}

@Composable
fun WorkoutTemplateCreationScreen(
    workoutTemplate: WorkoutTemplateInput,
    onWorkoutTemplateUpdate: ((WorkoutTemplateInput) -> WorkoutTemplateInput) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    onAddExercise: () -> Unit
) {
    val actions = listOf(
        TextAction(
            titleRes = CoreR.string.core_designsystem_save,
            onClick = onSaveClick
        )
    )
    Scaffold(
        topBar = {
            OptimalTopAppBar(
                title = stringResource(R.string.feature_workout_templates_create_title),
                navigationIcon = NavigationIcon.Close,
                onBackClick = onBackClick,
                actions = actions
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                value = workoutTemplate.name,
                onValueChange = { newName ->
                    onWorkoutTemplateUpdate { it.copy(name = newName) }
                },
                placeholder = {
                    Text(
                        text = "Workout Name",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                textStyle = MaterialTheme.typography.headlineMedium,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Number of exercises: ${workoutTemplate.exercises.size}")
            Spacer(modifier = Modifier.height(16.dp))
            DarkButton(
                text = stringResource(R.string.feature_workout_templates_create_add_exercise),
                onClick = onAddExercise,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}

@Preview
@Composable
fun WorkoutTemplateCreationScreenPreview() {
    OptimalTheme {
        WorkoutTemplateCreationScreen(
            workoutTemplate = WorkoutTemplateInput("", emptyList()),
            onWorkoutTemplateUpdate = {},
            onSaveClick = {},
            onBackClick = {},
            onAddExercise = {}
        )
    }
}
