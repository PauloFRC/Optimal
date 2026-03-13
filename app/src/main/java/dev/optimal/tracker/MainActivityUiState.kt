package dev.optimal.tracker

import dev.optimal.tracker.model.workout.WorkoutSessionModel

data class MainActivityUiState(
    val sessionHistory: List<WorkoutSessionModel> = listOf()
)