package dev.optimal.tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.optimal.tracker.data.local.model.workout.ModelExercise
import dev.optimal.tracker.data.local.model.workout.ModelSet
import dev.optimal.tracker.data.local.model.workout.SessionExercise
import dev.optimal.tracker.data.local.model.workout.SessionSet
import dev.optimal.tracker.data.local.model.workout.WorkoutModel
import dev.optimal.tracker.data.local.model.workout.WorkoutModelWithExercises
import dev.optimal.tracker.data.local.model.workout.WorkoutSession
import dev.optimal.tracker.data.local.model.workout.WorkoutSessionWithExercises
import java.util.Date

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


@Dao
interface WorkoutSessionDao {

    @Transaction
    @Query("SELECT * FROM WorkoutSession WHERE workoutSessionId = :workoutSessionId")
    suspend fun getWorkoutSessionWithExercises(workoutSessionId: Long): WorkoutSessionWithExercises?

    @Transaction
    @Query("SELECT * FROM WorkoutSession ORDER BY startDate DESC")
    suspend fun getAllWorkoutSessionsWithExercises(): List<WorkoutSessionWithExercises>

    @Transaction
    @Query("SELECT * FROM WorkoutSession WHERE workoutModelId = :workoutModelId ORDER BY startDate DESC")
    suspend fun getWorkoutSessionsByModelId(workoutModelId: Long): List<WorkoutSessionWithExercises>

    @Transaction
    @Query("SELECT * FROM WorkoutSession WHERE startDate BETWEEN :startDate AND :endDate ORDER BY startDate DESC")
    suspend fun getWorkoutSessionsByDateRange(startDate: Long, endDate: Long): List<WorkoutSessionWithExercises>

    @Transaction
    suspend fun getWorkoutSessionsByDateRange(startDate: Date, endDate: Date): List<WorkoutSessionWithExercises> {
        return getWorkoutSessionsByDateRange(startDate.time, endDate.time)
    }

    @Transaction
    @Query("SELECT * FROM WorkoutSession ORDER BY startDate DESC LIMIT :limit")
    suspend fun getRecentWorkoutSessions(limit: Long): List<WorkoutSessionWithExercises>

    @Transaction
    @Query("SELECT * FROM WorkoutSession WHERE name LIKE '%' || :searchTerm || '%' ORDER BY startDate DESC")
    suspend fun searchWorkoutSessionsByName(searchTerm: String): List<WorkoutSessionWithExercises>

    // Basic CRUD operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutSession(workoutSession: WorkoutSession): Long

    @Update
    suspend fun updateWorkoutSession(workoutSession: WorkoutSession)

    @Delete
    suspend fun deleteWorkoutSession(workoutSession: WorkoutSession)

    @Query("DELETE FROM WorkoutSession WHERE workoutSessionId = :workoutSessionId")
    suspend fun deleteWorkoutSessionById(workoutSessionId: Long)

    @Query("SELECT * FROM WorkoutSession ORDER BY startDate DESC")
    suspend fun getAllWorkoutSessions(): List<WorkoutSession>

    @Query("SELECT * FROM WorkoutSession WHERE workoutSessionId = :workoutSessionId")
    suspend fun getWorkoutSessionById(workoutSessionId: Long): WorkoutSession?

    @Transaction
    suspend fun insertCompleteWorkoutSession(
        workoutSession: WorkoutSession,
        sessionExercises: List<SessionExercise>,
        sessionSets: List<SessionSet>
    ): Long {
        val workoutSessionId = insertWorkoutSession(workoutSession)

        // Insert session exercises with the generated workout session ID
        val insertedSessionExercises = mutableListOf<SessionExercise>()
        sessionExercises.forEach { sessionExercise ->
            val sessionExerciseId = insertSessionExercise(
                sessionExercise.copy(workoutSessionId = workoutSessionId)
            )
            insertedSessionExercises.add(sessionExercise.copy(
                sessionExerciseId = sessionExerciseId,
                workoutSessionId = workoutSessionId
            ))
        }

        // Insert session sets with the correct session exercise IDs
        sessionSets.forEach { sessionSet ->
            val correspondingSessionExercise = insertedSessionExercises.find {
                it.order == sessionExercises.find { se ->
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
    suspend fun insertSessionExercise(sessionExercise: SessionExercise): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSessionSet(sessionSet: SessionSet): Long

    @Update
    suspend fun updateSessionSet(sessionSet: SessionSet)

    @Delete
    suspend fun deleteSessionSet(sessionSet: SessionSet)

    @Query("""
        UPDATE SessionSet 
        SET reps = :reps, weight = :weight, rir = :rir 
        WHERE sessionSetId = :sessionSetId
    """)
    suspend fun updateSessionSetPerformance(
        sessionSetId: Long,
        reps: Int?,
        weight: Double?,
        rir: Int?
    )

    @Query("UPDATE SessionSet SET completed = 1 WHERE sessionSetId = :sessionSetId")
    suspend fun markSessionSetCompleted(sessionSetId: Long)
}
