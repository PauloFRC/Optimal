package dev.optimal.tracker.model.workout

import dev.optimal.tracker.model.workout.enums.SetType

data class SessionExerciseModel(
    val id: Long,
    val name: String,
    val sets: List<SessionSetModel>
)

fun SessionExerciseModel.getBestSet(): SessionSetModel? {
    return sets.filter { it.isCompleted && it.weight != null }
        .maxByOrNull { it.weight!! }
}

fun SessionExerciseModel.getWorkingSets() : List<SessionSetModel> {
    return sets.filter { it.type == SetType.WORKING && it.isCompleted }
}
