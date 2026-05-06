package dev.optimal.tracker.workout.exercise

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.optimal.tracker.core.ui.components.OptimalTopAppBar

@Composable
fun ExerciseSelectionScreen(
    onExerciseSelected: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    val exercises = listOf(
        1L to "Bench Press",
        2L to "Squat",
        3L to "Deadlift",
        4L to "Overhead Press",
        5L to "Pull Up",
        6L to "Barbell Row"
    )

    Scaffold(
        topBar = {
            OptimalTopAppBar(
                title = "Select Exercise",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(exercises) { (id, name) ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onExerciseSelected(id) }
                        .padding(16.dp)
                ) {
                    Text(text = name, style = MaterialTheme.typography.bodyLarge)
                }
                HorizontalDivider()
            }
        }
    }
}
