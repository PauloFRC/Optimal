package dev.optimal.tracker.database.model.workout.input

import dev.optimal.tracker.model.workout.enums.SetType

data class ModelSetInput(
    val order: Int,
    val type: SetType
)