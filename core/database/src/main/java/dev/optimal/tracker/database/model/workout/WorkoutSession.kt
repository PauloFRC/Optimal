package dev.optimal.tracker.database.model.workout

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

@Entity(indices = [Index("workoutModelId")])
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true) val workoutSessionId: Long = 0,
    val workoutModelId: Long?,
    val name: String,
    val completed: Boolean?,
    val startDate: Date,
    val endDate: Date?
)

data class WorkoutSessionWithExercises(
    @Embedded val workoutSession: WorkoutSession,
    @Relation(
        entity = SessionExercise::class,
        parentColumn = "workoutSessionId",
        entityColumn = "workoutSessionId"
    )
    val sessionExercisesWithSets: List<SessionExerciseWithSets>
)
