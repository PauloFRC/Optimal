package dev.optimal.tracker.model.workout

import java.util.Date

data class WorkoutSessionModel(
    val id: Long,
    val workoutModelId: Long?,
    val name: String,
    val isCompleted: Boolean,
    val startDate: Date,
    val endDate: Date?,
    val exercises: List<SessionExerciseModel>
)
