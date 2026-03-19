package dev.optimal.tracker.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun ProfileScreenRoute(
    onStartWorkoutClick: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState()
    HomeScreen(
        uiState = uiState.value,
        onStartWorkoutClick = onStartWorkoutClick
    )
}

@Composable
fun HomeScreen(
    uiState: ProfileState,
    onStartWorkoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        uiState = ProfileState(),
        onStartWorkoutClick = {}
    )
}