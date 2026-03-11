package dev.optimal.tracker.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.optimal.tracker.database.model.workout.ModelExerciseEntity
import dev.optimal.tracker.database.model.workout.ModelSetEntity
import dev.optimal.tracker.database.model.workout.WorkoutModelEntity
import dev.optimal.tracker.database.model.workout.WorkoutModelWithExercises
import dev.optimal.tracker.database.model.workout.input.ModelExerciseInput
import dev.optimal.tracker.database.model.workout.input.WorkoutModelInput
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutModelDao {

    @Transaction
    @Query("SELECT * FROM WorkoutModelEntity WHERE workoutModelId = :workoutModelId")
    fun getWorkoutModelWithExercises(workoutModelId: Long): Flow<WorkoutModelWithExercises?>

    @Transaction
    @Query("SELECT * FROM WorkoutModelEntity ORDER BY name ASC")
    fun getAllWorkoutModelsWithExercises(): Flow<List<WorkoutModelWithExercises>>

    @Transaction
    @Query("SELECT * FROM WorkoutModelEntity WHERE name LIKE '%' || :searchTerm || '%' ORDER BY name ASC")
    fun searchWorkoutModelsByName(searchTerm: String): Flow<List<WorkoutModelWithExercises>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWorkoutModel(workoutModelEntity: WorkoutModelEntity): Long

    @Update
    suspend fun updateWorkoutModel(workoutModelEntity: WorkoutModelEntity)

    @Query("DELETE FROM WorkoutModelEntity WHERE workoutModelId = :workoutModelId")
    suspend fun deleteWorkoutModelById(workoutModelId: Long)

    @Query("DELETE FROM ModelExerciseEntity WHERE workoutModelId = :workoutModelId")
    suspend fun deleteModelExercisesByWorkoutId(workoutModelId: Long)

    @Transaction
    suspend fun insertCompleteWorkoutModel(input: WorkoutModelInput): Long {
        val workoutModelEntityId = insertWorkoutModel(WorkoutModelEntity(name = input.name))
        insertExercisesWithSets(workoutModelEntityId, input.exercises)
        return workoutModelEntityId
    }

    @Transaction
    suspend fun updateCompleteWorkoutModel(workoutModelId: Long, input: WorkoutModelInput) {
        updateWorkoutModel(WorkoutModelEntity(workoutModelId = workoutModelId, name = input.name))
        deleteModelExercisesByWorkoutId(workoutModelId)
        insertExercisesWithSets(workoutModelId, input.exercises)
    }

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertModelExercise(modelExerciseEntity: ModelExerciseEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertModelSet(modelSetEntity: ModelSetEntity): Long

    private suspend fun insertExercisesWithSets(
        workoutModelId: Long,
        exercises: List<ModelExerciseInput>
    ) {
        exercises.forEach { exerciseInput ->
            val modelExerciseEntityId = insertModelExercise(
                ModelExerciseEntity(
                    workoutModelId = workoutModelId,
                    exerciseId = exerciseInput.exerciseId,
                    order = exerciseInput.order
                )
            )
            exerciseInput.sets.forEach { setInput ->
                insertModelSet(
                    ModelSetEntity(
                        modelExerciseId = modelExerciseEntityId,
                        order = setInput.order,
                        type = setInput.type
                    )
                )
            }
        }
    }
}
