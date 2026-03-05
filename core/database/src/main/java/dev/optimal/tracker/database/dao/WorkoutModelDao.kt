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
import dev.optimal.tracker.database.model.workout.input.ModelExerciseInput
import dev.optimal.tracker.database.model.workout.input.WorkoutModelInput
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutModelDao {

    @Transaction
    @Query("SELECT * FROM WorkoutModel WHERE workoutModelId = :workoutModelId")
    fun getWorkoutModelWithExercises(workoutModelId: Long): Flow<WorkoutModelWithExercises?>

    @Transaction
    @Query("SELECT * FROM WorkoutModel ORDER BY name ASC")
    fun getAllWorkoutModelsWithExercises(): Flow<List<WorkoutModelWithExercises>>

    @Transaction
    @Query("SELECT * FROM WorkoutModel WHERE name LIKE '%' || :searchTerm || '%' ORDER BY name ASC")
    fun searchWorkoutModelsByName(searchTerm: String): Flow<List<WorkoutModelWithExercises>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWorkoutModel(workoutModel: WorkoutModel): Long

    @Update
    suspend fun updateWorkoutModel(workoutModel: WorkoutModel)

    @Query("DELETE FROM WorkoutModel WHERE workoutModelId = :workoutModelId")
    suspend fun deleteWorkoutModelById(workoutModelId: Long)

    @Query("DELETE FROM ModelExercise WHERE workoutModelId = :workoutModelId")
    suspend fun deleteModelExercisesByWorkoutId(workoutModelId: Long)

    @Transaction
    suspend fun insertCompleteWorkoutModel(input: WorkoutModelInput): Long {
        val workoutModelId = insertWorkoutModel(WorkoutModel(name = input.name))
        insertExercisesWithSets(workoutModelId, input.exercises)
        return workoutModelId
    }

    @Transaction
    suspend fun updateCompleteWorkoutModel(workoutModelId: Long, input: WorkoutModelInput) {
        updateWorkoutModel(WorkoutModel(workoutModelId = workoutModelId, name = input.name))
        deleteModelExercisesByWorkoutId(workoutModelId)
        insertExercisesWithSets(workoutModelId, input.exercises)
    }

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertModelExercise(modelExercise: ModelExercise): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertModelSet(modelSet: ModelSet): Long

    private suspend fun insertExercisesWithSets(
        workoutModelId: Long,
        exercises: List<ModelExerciseInput>
    ) {
        exercises.forEach { exerciseInput ->
            val modelExerciseId = insertModelExercise(
                ModelExercise(
                    workoutModelId = workoutModelId,
                    exerciseId = exerciseInput.exerciseId,
                    order = exerciseInput.order
                )
            )
            exerciseInput.sets.forEach { setInput ->
                insertModelSet(
                    ModelSet(
                        modelExerciseId = modelExerciseId,
                        order = setInput.order,
                        type = setInput.type
                    )
                )
            }
        }
    }
}
