package dev.optimal.tracker.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.optimal.tracker.database.model.workout.TemplateExerciseEntity
import dev.optimal.tracker.database.model.workout.TemplateSetEntity
import dev.optimal.tracker.database.model.workout.WorkoutTemplateEntity
import dev.optimal.tracker.database.model.workout.WorkoutTemplateWithExercises
import dev.optimal.tracker.database.model.workout.input.ModelExerciseInput
import dev.optimal.tracker.database.model.workout.input.WorkoutModelInput
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutTemplateDao {

    @Transaction
    @Query("SELECT * FROM WorkoutTemplateEntity WHERE workoutTemplateId = :workoutModelId")
    fun getWorkoutTemplateWithExercises(workoutModelId: Long): WorkoutTemplateWithExercises?

    @Transaction
    @Query("SELECT * FROM WorkoutTemplateEntity ORDER BY name ASC")
    fun getAllWorkoutTemplatesWithExercises(): Flow<List<WorkoutTemplateWithExercises>>

    @Transaction
    @Query("SELECT * FROM WorkoutTemplateEntity WHERE name LIKE '%' || :searchTerm || '%' ORDER BY name ASC")
    fun searchWorkoutTemplatesByName(searchTerm: String): Flow<List<WorkoutTemplateWithExercises>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWorkoutTemplate(workoutTemplateEntity: WorkoutTemplateEntity): Long

    @Update
    suspend fun updateWorkoutTemplate(workoutTemplateEntity: WorkoutTemplateEntity)

    @Query("DELETE FROM WorkoutTemplateEntity WHERE workoutTemplateId = :workoutModelId")
    suspend fun deleteWorkoutTemplateById(workoutModelId: Long)

    @Query("DELETE FROM TemplateExerciseEntity WHERE workoutTemplateId = :workoutModelId")
    suspend fun deleteTemplateExercisesByWorkoutId(workoutModelId: Long)

    @Transaction
    suspend fun insertCompleteWorkoutTemplate(input: WorkoutModelInput): Long {
        val workoutTemplateEntityId = insertWorkoutTemplate(WorkoutTemplateEntity(name = input.name))
        insertExercisesWithSets(workoutTemplateEntityId, input.exercises)
        return workoutTemplateEntityId
    }

    @Transaction
    suspend fun updateCompleteWorkoutTemplate(workoutModelId: Long, input: WorkoutModelInput) {
        updateWorkoutTemplate(WorkoutTemplateEntity(workoutTemplateId = workoutModelId, name = input.name))
        deleteTemplateExercisesByWorkoutId(workoutModelId)
        insertExercisesWithSets(workoutModelId, input.exercises)
    }

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTemplateExercise(templateExerciseEntity: TemplateExerciseEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTemplateSet(templateSetEntity: TemplateSetEntity): Long

    private suspend fun insertExercisesWithSets(
        workoutModelId: Long,
        exercises: List<ModelExerciseInput>
    ) {
        exercises.forEach { exerciseInput ->
            val templateExerciseEntityId = insertTemplateExercise(
                TemplateExerciseEntity(
                    workoutTemplateId = workoutModelId,
                    exerciseId = exerciseInput.exerciseId,
                    order = exerciseInput.order
                )
            )
            exerciseInput.sets.forEach { setInput ->
                insertTemplateSet(
                    TemplateSetEntity(
                        templateExerciseId = templateExerciseEntityId,
                        order = setInput.order,
                        type = setInput.type
                    )
                )
            }
        }
    }
}
