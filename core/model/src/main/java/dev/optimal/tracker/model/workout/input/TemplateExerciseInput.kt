package dev.optimal.tracker.model.workout.input

data class TemplateExerciseInput(
    val exerciseId: Long,
    val order: Int,
    val sets: List<TemplateSetInput>
)