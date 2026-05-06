package dev.optimal.tracker.model.workout.input

import dev.optimal.tracker.model.workout.enums.SetType

data class TemplateSetInput(
    val order: Int,
    val type: SetType
)