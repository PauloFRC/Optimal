package dev.optimal.tracker.model.workout

data class SessionExerciseModel(
    val id: Long,
    val name: String,
    val sets: List<SessionSetModel>
)
