package dev.optimal.tracker.workout.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.optimal.tracker.designsystem.theme.OptimalTheme
import dev.optimal.tracker.model.workout.TemplateExerciseModel
import dev.optimal.tracker.model.workout.TemplateSetModel
import dev.optimal.tracker.model.workout.WorkoutTemplateModel
import dev.optimal.tracker.model.workout.enums.SetType
import dev.optimal.tracker.model.workout.getWorkingSets
import dev.optimal.tracker.core.designsystem.R as CoreR

@Composable
fun WorkoutTemplateCard(
    workoutTemplate: WorkoutTemplateModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var moreExpanded by remember { mutableStateOf(false) }
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .height(20.dp)
            ) {
                Text(
                    text = workoutTemplate.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                IconButton(
                    onClick = { moreExpanded = !moreExpanded },
                    modifier = Modifier.offset(x = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(
                            CoreR.string.core_designsystem_dropdown_description
                        ),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            //TODO: integrar e melhorar pra shimmer text ficar a direita
//            ShimmerText(
//                text = stringResource(R.string.feature_workout_templates_last_executed),
//                isLoading = true,
//                style = MaterialTheme.typography.labelMedium
//            )

            workoutTemplate.exercises.forEach { workoutTemplate ->
                if (workoutTemplate.getWorkingSets().isNotEmpty()) {
                    Text(
                        text = "${workoutTemplate.getWorkingSets().size} x ${workoutTemplate.name}",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun WorkoutTemplateCardPreview() {
    OptimalTheme {
        WorkoutTemplateCard(
            workoutTemplate = WorkoutTemplateModel(
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
            )
        )
    }
}
