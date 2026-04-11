package dev.optimal.tracker.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import dev.optimal.tracker.core.ui.components.OptimalTopAppBar
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
        Text(
            text = "TELA DE WORKOUTTTT",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "TELA DE WORKOUTTTT",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "TELA DE WORKOUTTTT",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "TELA DE WORKOUTTTT",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "TELA DE WORKOUTTTT",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "TELA DE WORKOUTTTT",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "TELA DE WORKOUTTTT",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "TELA DE WORKOUTTTT",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutScreenPreview() {
    WorkoutScreen(
        uiState = WorkoutState(),
        onNavigateToSession = {},
        onNavigateToDetail = {},
        onNavigateBack = {}
    )
}