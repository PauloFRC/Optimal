package dev.optimal.tracker.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.optimal.tracker.database.model.workout.SessionExerciseEntity
import dev.optimal.tracker.database.model.workout.SessionSetEntity
import dev.optimal.tracker.database.model.workout.WorkoutSessionEntity
import dev.optimal.tracker.database.model.workout.WorkoutSessionWithExercises
import java.util.Date

@Dao
interface WorkoutSessionDao {

    @Transaction
    @Query("SELECT * FROM WorkoutSessionEntity WHERE workoutSessionId = :workoutSessionId")
    suspend fun getWorkoutSessionWithExercises(workoutSessionId: Long): WorkoutSessionWithExercises?

    @Transaction
    @Query("SELECT * FROM WorkoutSessionEntity ORDER BY startDate DESC")
    suspend fun getAllWorkoutSessionsWithExercises(): List<WorkoutSessionWithExercises>

    @Transaction
    @Query("SELECT * FROM WorkoutSessionEntity WHERE workoutModelId = :workoutModelId ORDER BY startDate DESC")
    suspend fun getWorkoutSessionsByModelId(workoutModelId: Long): List<WorkoutSessionWithExercises>

    @Transaction
    @Query("SELECT * FROM WorkoutSessionEntity WHERE startDate BETWEEN :startDate AND :endDate ORDER BY startDate DESC")
    suspend fun getWorkoutSessionsByDateRange(startDate: Long, endDate: Long): List<WorkoutSessionWithExercises>

    @Transaction
    suspend fun getWorkoutSessionsByDateRange(startDate: Date, endDate: Date): List<WorkoutSessionWithExercises> {
        return getWorkoutSessionsByDateRange(startDate.time, endDate.time)
    }

    @Transaction
    @Query("SELECT * FROM WorkoutSessionEntity ORDER BY startDate DESC LIMIT :limit")
    suspend fun getRecentWorkoutSessions(limit: Long): List<WorkoutSessionWithExercises>

    @Transaction
    @Query("SELECT * FROM WorkoutSessionEntity WHERE name LIKE '%' || :searchTerm || '%' ORDER BY startDate DESC")
    suspend fun searchWorkoutSessionsByName(searchTerm: String): List<WorkoutSessionWithExercises>

    // Basic CRUD operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutSession(workoutSessionEntity: WorkoutSessionEntity): Long

    @Update
    suspend fun updateWorkoutSession(workoutSessionEntity: WorkoutSessionEntity)

    @Delete
    suspend fun deleteWorkoutSession(workoutSessionEntity: WorkoutSessionEntity)

    @Query("DELETE FROM WorkoutSessionEntity WHERE workoutSessionId = :workoutSessionId")
    suspend fun deleteWorkoutSessionById(workoutSessionId: Long)

    @Query("SELECT * FROM WorkoutSessionEntity ORDER BY startDate DESC")
    suspend fun getAllWorkoutSessions(): List<WorkoutSessionEntity>

    @Query("SELECT * FROM WorkoutSessionEntity WHERE workoutSessionId = :workoutSessionId")
    suspend fun getWorkoutSessionById(workoutSessionId: Long): WorkoutSessionEntity?

    @Transaction
    suspend fun insertCompleteWorkoutSession(
        workoutSessionEntity: WorkoutSessionEntity,
        sessionExerciseEntities: List<SessionExerciseEntity>,
        sessionSetEntities: List<SessionSetEntity>
    ): Long {
        val workoutSessionId = insertWorkoutSession(workoutSessionEntity)

        // Insert session exercises with the generated workout session ID
        val insertedSessionExerciseEntities = mutableListOf<SessionExerciseEntity>()
        sessionExerciseEntities.forEach { sessionExercise ->
            val sessionExerciseId = insertSessionExercise(
                sessionExercise.copy(workoutSessionId = workoutSessionId)
            )
            insertedSessionExerciseEntities.add(sessionExercise.copy(
                sessionExerciseId = sessionExerciseId,
                workoutSessionId = workoutSessionId
            ))
        }

        // Insert session sets with the correct session exercise IDs
        sessionSetEntities.forEach { sessionSet ->
            val correspondingSessionExercise = insertedSessionExerciseEntities.find {
                it.order == sessionExerciseEntities.find { se ->
                    sessionSet.sessionExerciseId == se.sessionExerciseId
                }?.order
            }
            correspondingSessionExercise?.let { sessionExercise ->
                insertSessionSet(sessionSet.copy(sessionExerciseId = sessionExercise.sessionExerciseId))
            }
        }

        return workoutSessionId
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessionExercise(sessionExerciseEntity: SessionExerciseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessionSet(sessionSetEntity: SessionSetEntity): Long

    @Update
    suspend fun updateSessionSet(sessionSetEntity: SessionSetEntity)

    @Delete
    suspend fun deleteSessionSet(sessionSetEntity: SessionSetEntity)

    @Query(
        """
        UPDATE SessionSetEntity 
        SET reps = :reps, weight = :weight, rir = :rir 
        WHERE sessionSetId = :sessionSetId
    """
    )
    suspend fun updateSessionSetPerformance(
        sessionSetId: Long,
        reps: Int?,
        weight: Double?,
        rir: Int?
    )

    @Query("UPDATE SessionSetEntity SET completed = 1 WHERE sessionSetId = :sessionSetId")
    suspend fun markSessionSetCompleted(sessionSetId: Long)
}
