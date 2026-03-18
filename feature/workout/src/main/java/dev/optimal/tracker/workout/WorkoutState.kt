package dev.optimal.tracker.workout

import dev.optimal.tracker.model.workout.WorkoutSessionTemplate

data class WorkoutState(
    val sessionTemplates: List<WorkoutSessionTemplate> = listOf()
)