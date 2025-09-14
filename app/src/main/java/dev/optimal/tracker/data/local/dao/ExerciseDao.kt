package dev.optimal.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.optimal.tracker.data.local.model.workout.Exercise
import dev.optimal.tracker.data.local.model.workout.ExerciseType
import dev.optimal.tracker.data.local.model.workout.ExerciseWithMuscleGroups

@Dao
interface ExerciseDao {

    @Transaction
    @Query("SELECT * FROM exercise WHERE exerciseId = :exerciseId")
    suspend fun getExerciseWithMuscleGroups(exerciseId: Long): ExerciseWithMuscleGroups?

    @Transaction
    @Query("SELECT * FROM exercise ORDER BY name ASC")
    suspend fun getAllExercisesWithMuscleGroups(): List<ExerciseWithMuscleGroups>

    @Transaction
    @Query("SELECT * FROM exercise WHERE name LIKE '%' || :searchTerm || '%' ORDER BY name ASC")
    suspend fun searchExercisesByName(searchTerm: String): List<ExerciseWithMuscleGroups>

    @Transaction
    @Query("SELECT * FROM exercise WHERE type = :exerciseType ORDER BY name ASC")
    suspend fun getExercisesByType(exerciseType: ExerciseType): List<ExerciseWithMuscleGroups>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise): Long

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("SELECT * FROM exercise ORDER BY name ASC")
    suspend fun getAllExercises(): List<Exercise>

    @Query("SELECT * FROM exercise WHERE exerciseId = :exerciseId")
    suspend fun getExerciseById(exerciseId: Long): Exercise?
}