package dev.optimal.tracker.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.optimal.tracker.designsystem.theme.OptimalTheme
import dev.optimal.tracker.model.workout.SessionExerciseModel
import dev.optimal.tracker.model.workout.WorkoutSessionModel
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SessionHistoryCard(
    session: WorkoutSessionModel,
    modifier: Modifier = Modifier
) {
    //TODO: move to utils and make year only appear in past years
    val duration = Duration.between(session.startDate, session.endDate)
    val hours = duration.toHours()
    val minutes = duration.toMinutesPart()
    val durationStr = String.format(Locale.US, "%dh%02dm", hours, minutes)
    val dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
    val startDateStr = session.startDate.toLocalDate().format(dateFormatter)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = session.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "3 Prs",
                    style = MaterialTheme.typography.labelSmall,
                ) //TODO
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row() {
                // Icon() TODO
                Text(
                    text = durationStr,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = " • ",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = startDateStr,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Column() {
                session.exercises.forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = it.sets.size.toString(),
                            style = MaterialTheme.typography.labelMedium
                        ) //TODO: get best set
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun SessionHistoryCardPreview() {
    val session = WorkoutSessionModel(
        id = 1,
        workoutModelId = 1,
        name = "Session Name",
        isCompleted = true,
        startDate = java.time.LocalDateTime.now(),
        endDate = java.time.LocalDateTime.now(),
        exercises = listOf(
            SessionExerciseModel(1, "Bench Press", listOf()),
            SessionExerciseModel(2, "Squat", listOf()),
            SessionExerciseModel(3, "Deadlift", listOf())
        )
    )
    OptimalTheme {
        SessionHistoryCard(
            session = session
        )
    }
}
