package dev.optimal.tracker.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.optimal.tracker.model.workout.enums.ExerciseType
import dev.optimal.tracker.database.model.workout.ExerciseEntity

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise WHERE exerciseId = :exerciseId")
    suspend fun getExerciseById(exerciseId: Long): ExerciseEntity?

    @Query("SELECT * FROM exercise ORDER BY name ASC")
    suspend fun getAllExercises(): List<ExerciseEntity>

    @Query("SELECT * FROM exercise WHERE name LIKE '%' || :searchTerm || '%' ORDER BY name ASC")
    suspend fun searchExercisesByName(searchTerm: String): List<ExerciseEntity>

    @Query("SELECT * FROM exercise WHERE type = :exerciseType ORDER BY name ASC")
    suspend fun getExercisesByType(exerciseType: ExerciseType): List<ExerciseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exerciseEntity: ExerciseEntity): Long

    @Update
    suspend fun updateExercise(exerciseEntity: ExerciseEntity)

    @Delete
    suspend fun deleteExercise(exerciseEntity: ExerciseEntity)
}
