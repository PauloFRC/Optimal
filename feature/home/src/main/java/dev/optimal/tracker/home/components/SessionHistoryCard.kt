package dev.optimal.tracker.home.components

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.optimal.tracker.designsystem.theme.MediumEmphasis
import dev.optimal.tracker.designsystem.theme.OptimalTheme
import dev.optimal.tracker.feature.home.R
import dev.optimal.tracker.model.workout.SessionExerciseModel
import dev.optimal.tracker.model.workout.SessionSetModel
import dev.optimal.tracker.model.workout.WorkoutSessionModel
import dev.optimal.tracker.model.workout.enums.SetType
import dev.optimal.tracker.model.workout.getBestSet
import dev.optimal.tracker.model.workout.getPersonalRecords
import dev.optimal.tracker.model.workout.getWorkingSets
import dev.optimal.tracker.utils.OptimalDateTimeFormatter
import java.util.Locale

@Composable
fun SessionHistoryCard(
    session: WorkoutSessionModel,
    modifier: Modifier = Modifier
) {
    val formattedDuration = session.endDate?.let {
        OptimalDateTimeFormatter.formatDuration(session.startDate, it)
    } ?: ""
    val formattedDate = OptimalDateTimeFormatter.formatDate(session.startDate)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
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

                val personalRecordsFormatted = session.getPersonalRecords().size
                    .takeIf { it > 0 }
                    ?.let { "$it PRs" } ?: ""
                Text(
                    text = personalRecordsFormatted,
                    style = MaterialTheme.typography.labelSmall,
                ) //TODO
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_time),
                    contentDescription = "time icon",
                    tint = MediumEmphasis,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = formattedDuration,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = " • ",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.feature_home_exercise),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(R.string.feature_home_best_set),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                session.exercises.forEach { exercise ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (exercise.getWorkingSets().isNotEmpty()) {
                            Text(
                                text = "${exercise.getWorkingSets().size} x ${exercise.name}",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                        exercise.getBestSet()?.let { set ->
                            val weightStr = set.weight?.let { w ->
                                if (w % 1.0 == 0.0) w.toInt().toString()
                                else String.format(Locale.US, "%,1f", w)
                            } ?: return@let
                            Text(
                                text = "$weightStr kg x ${set.reps}",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SessionHistoryCardPreview() {
    val set1 = SessionSetModel(
        id = 1,
        order = 1,
        type = SetType.WORKING,
        isCompleted = true,
        reps = 10,
        weight = 100.0,
        rir = null
    )
    val set2 = SessionSetModel(
        id = 1,
        order = 1,
        type = SetType.WORKING,
        isCompleted = true,
        reps = 10,
        weight = 90.0,
        rir = null
    )
    val session = WorkoutSessionModel(
        id = 1,
        workoutModelId = 1,
        name = "Session Name",
        isCompleted = true,
        startDate = java.time.LocalDateTime.now(),
        endDate = java.time.LocalDateTime.now(),
        exercises = listOf(
            SessionExerciseModel(1, "Bench Press", listOf(set1, set2)),
            SessionExerciseModel(2, "Squat", listOf(set1, set2)),
            SessionExerciseModel(3, "Deadlift", listOf(set1, set2))
        )
    )
    OptimalTheme {
        SessionHistoryCard(
            session = session
        )
    }
}
