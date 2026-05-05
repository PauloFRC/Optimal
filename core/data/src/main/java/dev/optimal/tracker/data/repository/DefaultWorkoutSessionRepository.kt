package dev.optimal.tracker.data.repository

import dev.optimal.tracker.data.model.toModel
import dev.optimal.tracker.database.dao.WorkoutSessionDao
import dev.optimal.tracker.database.dao.WorkoutTemplateDao
import dev.optimal.tracker.model.workout.SessionExerciseModel
import dev.optimal.tracker.model.workout.SessionSetModel
import dev.optimal.tracker.model.workout.WorkoutSessionModel
import dev.optimal.tracker.model.workout.WorkoutTemplateModel
import dev.optimal.tracker.model.workout.enums.SetType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

internal class DefaultWorkoutSessionRepository @Inject constructor(
    private val workoutSessionDao: WorkoutSessionDao,
    private val workoutTemplateDao: WorkoutTemplateDao
): WorkoutSessionRepository {
    override fun getOrderedWorkoutSessions(): Flow<List<WorkoutSessionModel>> {
        return workoutSessionDao.getAllWorkoutSessionsWithExercises().map { sessions ->
            sessions.map { it.toModel() }
        }
    }

    override suspend fun getWorkoutSessionById(id: Long): WorkoutSessionModel? {
        val sets = (1..30).map { index ->
            SessionSetModel(
                id = index.toLong(),
                order = index,
                type = if (index == 1) SetType.WARMUP else SetType.WORKING,
                weight = 100.0,
                reps = 10,
                isCompleted = true,
                rir = 0
            )
        }
        return WorkoutSessionModel(
            id = 1,
            workoutModelId = 1,
            name = "Session Name",
            isCompleted = true,
            startDate = LocalDateTime.of(2026, 3, 19, 14, 30),
            endDate = LocalDateTime.of(2026, 3, 19, 14, 30),
            exercises = listOf(
                SessionExerciseModel(
                    1,
                    "Bench Press",
                    sets = sets
                    )
                )
        )
//        return workoutSessionDao.getWorkoutSessionWithExercises(id)?.toModel()
    }

    override fun getWorkoutTemplates(): Flow<List<WorkoutTemplateModel>> {
        return workoutTemplateDao.getAllWorkoutTemplatesWithExercises().map { templates ->
            templates.map { it.toModel() }
        }
    }

    override suspend fun getWorkoutTemplateById(id: Long): WorkoutTemplateModel? {
        return workoutTemplateDao.getWorkoutTemplateWithExercises(id)?.toModel()
    }
}
