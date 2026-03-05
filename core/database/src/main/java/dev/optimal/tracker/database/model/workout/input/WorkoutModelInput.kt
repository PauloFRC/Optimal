package dev.optimal.tracker.database.model.workout.input

data class WorkoutModelInput(
    val name: String,
    val exercises: List<ModelExerciseInput>
)