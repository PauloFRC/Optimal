package dev.optimal.tracker.database.model.workout

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.optimal.tracker.model.workout.enums.SetType

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ModelExerciseEntity::class,
            parentColumns = ["modelExerciseId"],
            childColumns = ["modelExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("modelExerciseId")]
)
data class ModelSetEntity (
    @PrimaryKey(autoGenerate = true) val modelSetId: Long = 0,
    val modelExerciseId: Long,
    val order: Int,
    val type: SetType
)
