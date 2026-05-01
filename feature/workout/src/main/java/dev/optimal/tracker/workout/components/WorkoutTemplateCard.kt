package dev.optimal.tracker.workout.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.optimal.tracker.designsystem.theme.OptimalTheme
import dev.optimal.tracker.model.workout.WorkoutTemplateModel

@Composable
fun WorkoutTemplateCard(
    workoutModel: WorkoutTemplateModel
) {

}

@Preview
@Composable
fun WorkoutTemplateCardPreview() {
    OptimalTheme {
        WorkoutTemplateCard(

        )
    }
}
