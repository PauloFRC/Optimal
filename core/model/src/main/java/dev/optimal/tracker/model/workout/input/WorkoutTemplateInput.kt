package dev.optimal.tracker.model.workout.input

data class WorkoutTemplateInput(
    val name: String,
    val exercises: List<TemplateExerciseInput>
)
