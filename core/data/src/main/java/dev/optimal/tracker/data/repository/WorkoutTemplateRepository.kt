package dev.optimal.tracker.data.repository

import dev.optimal.tracker.database.model.workout.WorkoutTemplateWithExercises
import dev.optimal.tracker.model.workout.input.WorkoutTemplateInput
import kotlinx.coroutines.flow.Flow

interface WorkoutTemplateRepository {
    fun getAllWorkoutTemplates(): Flow<List<WorkoutTemplateWithExercises>>
    fun searchWorkoutTemplates(searchTerm: String): Flow<List<WorkoutTemplateWithExercises>>
    suspend fun getWorkoutTemplateById(workoutTemplateId: Long): WorkoutTemplateWithExercises?
    suspend fun addWorkoutTemplate(input: WorkoutTemplateInput): Long
    suspend fun editWorkoutTemplate(workoutTemplateId: Long, input: WorkoutTemplateInput)
    suspend fun deleteWorkoutTemplate(workoutTemplateId: Long)
}
