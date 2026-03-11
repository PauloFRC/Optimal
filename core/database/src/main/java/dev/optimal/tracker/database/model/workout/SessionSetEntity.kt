package dev.optimal.tracker.database.model.workout

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.optimal.tracker.database.enums.SetType

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = SessionExerciseEntity::class,
            parentColumns = ["sessionExerciseId"],
            childColumns = ["sessionExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sessionExerciseId")]
)
data class SessionSetEntity (
    @PrimaryKey(autoGenerate = true) val sessionSetId: Long = 0,
    val sessionExerciseId: Long,
    val order: Int,
    val type: SetType,
    val completed: Boolean = false,
    val reps: Int? = null,
    val weight: Double? = null,
    val durationSeconds: Int? = null,
    val rir: Int? = null
)
