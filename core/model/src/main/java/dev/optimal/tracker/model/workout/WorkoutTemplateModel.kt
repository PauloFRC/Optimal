package dev.optimal.tracker.model.workout

data class WorkoutTemplateModel(
    val id: Long,
    val name: String,
    val exercises: List<TemplateExerciseModel>
)
