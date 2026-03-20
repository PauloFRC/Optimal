package dev.optimal.tracker.model.workout

import dev.optimal.tracker.model.workout.enums.SetType

data class SessionSetModel(
    val id: Long,
    val order: Int,
    val type: SetType,
    val isCompleted: Boolean,
    val reps: Int?,
    val weight: Double?,
    val rir: Int?
)