package dev.optimal.tracker.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.optimal.tracker.designsystem.theme.OptimalTheme
import dev.optimal.tracker.home.components.SessionHistoryCard
import dev.optimal.tracker.model.workout.WorkoutSessionModel
import dev.optimal.tracker.utils.OptimalDateTimeFormatter

@Composable
fun HomeScreenRoute(
    onStartWorkoutClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState,
        onStartWorkoutClick = onStartWorkoutClick
    )
}

@Composable
fun HomeScreen(
    uiState: HomeState,
    onStartWorkoutClick: () -> Unit
) {
    val sessions = uiState.sessionHistory
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        itemsIndexed(sessions) { index, session ->
            MonthIndicator(
                currentSession = session,
                previousSession = sessions.getOrNull(index - 1)
            )
            SessionHistoryCard(
                session = session,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun MonthIndicator(
    currentSession: WorkoutSessionModel,
    previousSession: WorkoutSessionModel?,
) {
    val isNewMonth = previousSession == null ||
            currentSession.startDate.month != previousSession.startDate.month ||
            currentSession.startDate.year != previousSession.startDate.year

    if (isNewMonth) {
        Text(
            text = OptimalDateTimeFormatter
                .formatMonth(currentSession.startDate)
                .uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.1.em),
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    OptimalTheme {
        HomeScreen(
            uiState = HomeState(),
            onStartWorkoutClick = {}
        )
    }
}
