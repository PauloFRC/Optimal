package dev.optimal.tracker.database.model.workout.input

data class ModelExerciseInput(
    val exerciseId: Long,
    val order: Int,
    val sets: List<ModelSetInput>
)