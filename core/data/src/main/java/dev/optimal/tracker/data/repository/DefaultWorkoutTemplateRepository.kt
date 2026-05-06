package dev.optimal.tracker.data.repository

import dev.optimal.tracker.database.dao.WorkoutTemplateDao
import dev.optimal.tracker.database.model.workout.WorkoutTemplateWithExercises
import dev.optimal.tracker.model.workout.input.WorkoutTemplateInput
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultWorkoutTemplateRepository @Inject constructor(
    private val workoutTemplateDao: WorkoutTemplateDao
) : WorkoutTemplateRepository {

    override fun getAllWorkoutTemplates(): Flow<List<WorkoutTemplateWithExercises>> =
        workoutTemplateDao.getAllWorkoutTemplatesWithExercises()

    override fun searchWorkoutTemplates(searchTerm: String): Flow<List<WorkoutTemplateWithExercises>> =
        workoutTemplateDao.searchWorkoutTemplatesByName(searchTerm)

    override suspend fun getWorkoutTemplateById(workoutTemplateId: Long): WorkoutTemplateWithExercises? =
        workoutTemplateDao.getWorkoutTemplateWithExercises(workoutTemplateId)

    override suspend fun addWorkoutTemplate(input: WorkoutTemplateInput): Long =
        workoutTemplateDao.insertCompleteWorkoutTemplate(input)

    override suspend fun editWorkoutTemplate(workoutTemplateId: Long, input: WorkoutTemplateInput) =
        workoutTemplateDao.updateCompleteWorkoutTemplate(workoutTemplateId, input)

    override suspend fun deleteWorkoutTemplate(workoutTemplateId: Long) =
        workoutTemplateDao.deleteWorkoutTemplateById(workoutTemplateId)
}
