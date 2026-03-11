package dev.optimal.tracker.database.model.workout

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDateTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = WorkoutModelEntity::class,
            parentColumns = ["workoutModelId"],
            childColumns = ["workoutModelId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("workoutModelId")]
)
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val workoutSessionId: Long = 0,
    val workoutModelId: Long?,
    val name: String,
    val completed: Boolean = false,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime?
)

data class WorkoutSessionWithExercises(
    @Embedded val workoutSessionEntity: WorkoutSessionEntity,
    @Relation(
        entity = SessionExerciseEntity::class,
        parentColumn = "workoutSessionId",
        entityColumn = "workoutSessionId"
    )
    val sessionExercisesWithSets: List<SessionExerciseWithSets>
)
