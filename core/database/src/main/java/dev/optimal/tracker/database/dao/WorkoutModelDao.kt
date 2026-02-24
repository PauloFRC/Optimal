package dev.optimal.tracker.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.optimal.tracker.database.model.workout.ModelExercise
import dev.optimal.tracker.database.model.workout.ModelSet
import dev.optimal.tracker.database.model.workout.WorkoutModel
import dev.optimal.tracker.database.model.workout.WorkoutModelWithExercises

@Dao
interface WorkoutModelDao {

    // Get single workout model with exercises and sets
    @Transaction
    @Query("SELECT * FROM WorkoutModel WHERE workoutModelId = :workoutModelId")
    suspend fun getWorkoutModelWithExercises(workoutModelId: Long): WorkoutModelWithExercises?

    // Get all workout models with exercises and sets
    @Transaction
    @Query("SELECT * FROM WorkoutModel ORDER BY name ASC")
    suspend fun getAllWorkoutModelsWithExercises(): List<WorkoutModelWithExercises>

    // Get workout models by name pattern
    @Transaction
    @Query("SELECT * FROM WorkoutModel WHERE name LIKE '%' || :searchTerm || '%' ORDER BY name ASC")
    suspend fun searchWorkoutModelsByName(searchTerm: String): List<WorkoutModelWithExercises>

    // Basic CRUD operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutModel(workoutModel: WorkoutModel): Long

    @Update
    suspend fun updateWorkoutModel(workoutModel: WorkoutModel)

    @Delete
    suspend fun deleteWorkoutModel(workoutModel: WorkoutModel)

    @Query("DELETE FROM WorkoutModel WHERE workoutModelId = :workoutModelId")
    suspend fun deleteWorkoutModelById(workoutModelId: Long)

    // Get just the workout models without exercises (for lists)
    @Query("SELECT * FROM WorkoutModel ORDER BY name ASC")
    suspend fun getAllWorkoutModels(): List<WorkoutModel>

    @Query("SELECT * FROM WorkoutModel WHERE workoutModelId = :workoutModelId")
    suspend fun getWorkoutModelById(workoutModelId: Long): WorkoutModel?

    @Query("SELECT * FROM ModelSet")
    suspend fun getAllModelSetsForDebugging(): List<ModelSet>

    // Transaction method to insert complete workout model with exercises and sets
    @Transaction
    suspend fun insertCompleteWorkoutModel(
        workoutModel: WorkoutModel,
        modelExercises: List<ModelExercise>,
        modelSets: List<ModelSet>
    ): Long {
        val workoutModelId = insertWorkoutModel(workoutModel)

        val insertedModelExercises = mutableListOf<ModelExercise>()
        modelExercises.forEach { modelExercise ->
            val modelExerciseId = insertModelExercise(
                modelExercise.copy(workoutModelId = workoutModelId)
            )
            insertedModelExercises.add(modelExercise.copy(
                modelExerciseId = modelExerciseId,
                workoutModelId = workoutModelId
            ))
        }

        // Insert model sets with the correct model exercise IDs
        modelSets.forEach { modelSet ->
            val correspondingModelExercise = insertedModelExercises.find {
                it.order == modelExercises.find { me ->
                    modelSet.modelExerciseId == me.modelExerciseId
                }?.order
            }
            correspondingModelExercise?.let { modelExercise ->
                insertModelSet(modelSet.copy(modelExerciseId = modelExercise.modelExerciseId))
            }
        }

        return workoutModelId
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModelExercise(modelExercise: ModelExercise): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModelSet(modelSet: ModelSet): Long
}
