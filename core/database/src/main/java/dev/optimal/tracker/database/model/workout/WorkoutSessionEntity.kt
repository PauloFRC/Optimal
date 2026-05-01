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
            entity = WorkoutTemplateEntity::class,
            parentColumns = ["workoutTemplateId"],
            childColumns = ["workoutTemplateId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("workoutTemplateId")]
)
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val workoutSessionId: Long = 0,
    val workoutTemplateId: Long?,
    val name: String,
    val completed: Boolean = false,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime?
)

data class WorkoutSessionWithExercises(
    @Embedded val workoutSession: WorkoutSessionEntity,
    @Relation(
        entity = SessionExerciseEntity::class,
        parentColumn = "workoutSessionId",
        entityColumn = "workoutSessionId"
    )
    val exercises: List<SessionExerciseWithSets>
)
