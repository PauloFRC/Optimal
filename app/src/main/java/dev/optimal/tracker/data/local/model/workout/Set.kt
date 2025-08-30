package dev.optimal.tracker.data.local.model.workout

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ModelExercise::class,
            parentColumns = ["modelExerciseId"],
            childColumns = ["modelExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ModelSet (
    @PrimaryKey(autoGenerate = true) val modelSetId: Long = 0,
    val modelExerciseId: Long,
    val order: Int,
    val type: SetType
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = SessionExercise::class,
            parentColumns = ["sessionExerciseId"],
            childColumns = ["sessionExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SessionSet (
    @PrimaryKey(autoGenerate = true) val sessionSetId: Long = 0,
    val sessionExerciseId: Long,
    val order: Int,
    val type: SetType,
    val completed: Boolean = false,
    val reps: Int? = null,
    val weight: Double? = null,
    val rir: Int? = null
)

enum class SetType {
    WARMUP,
    WORKING
}
