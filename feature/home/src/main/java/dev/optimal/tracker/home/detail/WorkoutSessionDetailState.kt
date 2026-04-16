package dev.optimal.tracker.home.detail

import dev.optimal.tracker.model.workout.WorkoutSessionModel

sealed interface WorkoutSessionDetailState {
    data object Loading : WorkoutSessionDetailState
    data class Success(
        val session: WorkoutSessionModel
    ) : WorkoutSessionDetailState
    data class Error(val message: String?) : WorkoutSessionDetailState
}
