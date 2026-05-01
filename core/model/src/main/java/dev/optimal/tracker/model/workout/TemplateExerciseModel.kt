package dev.optimal.tracker.model.workout

import dev.optimal.tracker.model.workout.enums.SetType

data class TemplateExerciseModel(
    val id: Long,
    val name: String,
    val sets: List<TemplateSetModel>
)

fun TemplateExerciseModel.getWorkingSets() : List<TemplateSetModel> {
    return sets.filter { it.type == SetType.WORKING }
}
