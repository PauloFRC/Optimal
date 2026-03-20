package dev.optimal.tracker.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.optimal.tracker.designsystem.theme.OptimalTheme
import dev.optimal.tracker.home.components.SessionHistoryCard

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
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        items(uiState.sessionHistory) { session ->
            SessionHistoryCard(
                session = session,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
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
