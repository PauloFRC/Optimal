package dev.optimal.tracker.data.repository

import dev.optimal.tracker.data.model.toModel
import dev.optimal.tracker.database.dao.WorkoutSessionDao
import dev.optimal.tracker.model.workout.SessionExerciseModel
import dev.optimal.tracker.model.workout.SessionSetModel
import dev.optimal.tracker.model.workout.WorkoutSessionModel
import dev.optimal.tracker.model.workout.enums.SetType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
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
//        return workoutSessionDao.getWorkoutSessionWithExercises(id)?.toModel()
        //TODO: remove
        val sets = List(30) { i ->
            val index = i + 1
            SessionSetModel(
                id = index.toLong(),
                order = index,
                type = SetType.WORKING,
                isCompleted = false,
                reps = 8 + (i % 3),
                weight = 180.0 + (index * 5),
                rir = null
            )
        }
        return WorkoutSessionModel(
            id = 1,
            workoutModelId = 1,
            name = "Session Name",
            isCompleted = true,
            startDate = LocalDateTime.of(2026, 3, 19, 14, 30),
            endDate = LocalDateTime.of(2026, 3, 19, 17, 15),
            exercises = listOf(
                SessionExerciseModel(
                    id = 1,
                    name = "Bench Press",
                    sets = listOf(
                        SessionSetModel(
                            id = 1,
                            order = 1,
                            type = SetType.WORKING,
                            isCompleted = true,
                            reps = 10,
                            weight = 100.0,
                            rir = null
                        ),
                        SessionSetModel(
                            id = 1,
                            order = 1,
                            type = SetType.WORKING,
                            isCompleted = true,
                            reps = 10,
                            weight = 100.0,
                            rir = null
                        ),
                    )
                ),
                SessionExerciseModel(
                    id = 1,
                    name = "Squats",
                    sets = sets
                )
            )
        )
    }
}
