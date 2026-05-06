package dev.optimal.tracker.workout.template

import dev.optimal.tracker.model.workout.input.WorkoutTemplateInput

data class WorkoutTemplateCreationState(
    val workoutTemplate: WorkoutTemplateInput = WorkoutTemplateInput(
        name = "",
        exercises = emptyList()
    ),
    val isSaving: Boolean = false
)
