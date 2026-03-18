package dev.optimal.tracker.home

import dev.optimal.tracker.model.workout.WorkoutSessionModel

data class HomeState(
    val sessionHistory: List<WorkoutSessionModel> = listOf()
)