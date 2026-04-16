package dev.optimal.tracker.data.repository

import dev.optimal.tracker.data.model.toModel
import dev.optimal.tracker.database.dao.WorkoutSessionDao
import dev.optimal.tracker.model.workout.WorkoutSessionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DefaultWorkoutSessionRepository @Inject constructor(
    private val workoutSessionDao: WorkoutSessionDao
): WorkoutSessionRepository {
    override fun getOrderedWorkoutSessions(): Flow<List<WorkoutSessionModel>> {
        return workoutSessionDao.getAllWorkoutSessionsWithExercises().map { sessions ->
            sessions.map { it.toModel() }
        }
    }

    override suspend fun getWorkoutSessionById(id: Long): WorkoutSessionModel? {
        return workoutSessionDao.getWorkoutSessionWithExercises(id)?.toModel()
    }
}
