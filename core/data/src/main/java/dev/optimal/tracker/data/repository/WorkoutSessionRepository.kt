package dev.optimal.tracker.data.repository

import dev.optimal.tracker.model.workout.WorkoutSessionModel
import dev.optimal.tracker.model.workout.WorkoutTemplateModel
import kotlinx.coroutines.flow.Flow

interface WorkoutSessionRepository {
    fun getOrderedWorkoutSessions(): Flow<List<WorkoutSessionModel>>
    suspend fun getWorkoutSessionById(id: Long): WorkoutSessionModel?

    fun getWorkoutTemplates(): Flow<List<WorkoutTemplateModel>>
    suspend fun getWorkoutTemplateById(id: Long): WorkoutTemplateModel?
}
