package dev.optimal.tracker.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.optimal.tracker.database.model.workout.Exercise
import dev.optimal.tracker.database.model.workout.ExerciseType
import dev.optimal.tracker.database.model.workout.MuscleGroupWithRole

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise WHERE exerciseId = :exerciseId")
    suspend fun getExerciseById(exerciseId: Long): Exercise?

    @Query("SELECT * FROM exercise ORDER BY name ASC")
    suspend fun getAllExercises(): List<Exercise>

    @Query("SELECT * FROM exercise WHERE name LIKE '%' || :searchTerm || '%' ORDER BY name ASC")
    suspend fun searchExercisesByName(searchTerm: String): List<Exercise>

    @Query("SELECT * FROM exercise WHERE type = :exerciseType ORDER BY name ASC")
    suspend fun getExercisesByType(exerciseType: ExerciseType): List<Exercise>

    @Query("""
        SELECT mg.*, xref.role 
        FROM muscle_group AS mg 
        INNER JOIN ExerciseMuscleGroupCrossRef AS xref 
        ON mg.muscleGroupId = xref.muscleGroupId 
        WHERE xref.exerciseId = :exerciseId
    """)
    suspend fun getMuscleGroupsForExercise(exerciseId: Long): List<MuscleGroupWithRole>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise): Long

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)
}
