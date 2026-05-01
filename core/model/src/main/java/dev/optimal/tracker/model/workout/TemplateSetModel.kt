package dev.optimal.tracker.model.workout

import dev.optimal.tracker.model.workout.enums.SetType

data class TemplateSetModel(
    val id: Long,
    val order: Int,
    val type: SetType,
)
