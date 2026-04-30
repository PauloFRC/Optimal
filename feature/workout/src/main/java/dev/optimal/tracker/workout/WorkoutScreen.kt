package dev.optimal.tracker.workout

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
import dev.optimal.tracker.core.ui.components.OptimalTopAppBar
import dev.optimal.tracker.designsystem.theme.OptimalTheme
import dev.optimal.tracker.feature.workout.R

@Composable
fun WorkoutScreenRoute(
    onNavigateToSession: () -> Unit,
    onNavigateToDetail: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: WorkoutViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        OptimalTopAppBar(
            title = stringResource(R.string.feature_workout_title)
        )

        WorkoutScreen(
            uiState = uiState.value,
            onNavigateToSession = onNavigateToSession,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateBack = onNavigateBack
        )
    }
}

@Composable
fun WorkoutScreen(
    uiState: WorkoutState,
    onNavigateToSession: () -> Unit,
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
            onClick = onNavigateToSession,
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
    }
}

@Preview
@Composable
fun WorkoutScreenPreview() {
    OptimalTheme {
        WorkoutScreen(
            uiState = WorkoutState(),
            onNavigateToSession = {},
            onNavigateToDetail = {},
            onNavigateBack = {}
        )
    }
}