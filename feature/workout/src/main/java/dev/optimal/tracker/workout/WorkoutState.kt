package dev.optimal.tracker.workout

import dev.optimal.tracker.model.workout.WorkoutTemplateModel

data class WorkoutState(
    val workoutTemplates: List<WorkoutTemplateModel> = listOf()
)