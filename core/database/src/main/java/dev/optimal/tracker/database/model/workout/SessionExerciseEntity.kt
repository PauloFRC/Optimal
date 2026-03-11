package dev.optimal.tracker.database.model.workout

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSessionEntity::class,
            parentColumns = ["workoutSessionId"],
            childColumns = ["workoutSessionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["exerciseId"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("workoutSessionId"),
        Index("exerciseId")
    ]
)
data class SessionExerciseEntity(
    @PrimaryKey(autoGenerate = true) val sessionExerciseId: Long = 0,
    val workoutSessionId: Long,
    val exerciseId: Long,
    val order: Int
)

data class SessionExerciseWithSets (
    @Embedded val sessionExerciseEntity: SessionExerciseEntity,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "exerciseId"
    )
    val exerciseEntity: ExerciseEntity,
    @Relation(
        entity = SessionSetEntity::class,
        parentColumn = "sessionExerciseId",
        entityColumn = "sessionExerciseId"
    )
    val sessionSetEntities: List<SessionSetEntity>
)
