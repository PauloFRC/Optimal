package dev.optimal.tracker.model.workout

import dev.optimal.tracker.database.model.workout.SetType

data class SessionSetModel(
    val id: Long,
    val exerciseId: Long,
    val order: Int,
    val type: SetType,
    val isCompleted: Boolean,
    val reps: Int?,
    val weight: Double?,
    val rir: Int?
)