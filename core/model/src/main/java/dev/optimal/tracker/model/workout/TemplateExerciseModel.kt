package dev.optimal.tracker.model.workout

data class TemplateExerciseModel(
    val id: Long,
    val name: String,
    val sets: List<TemplateSetModel>
)
