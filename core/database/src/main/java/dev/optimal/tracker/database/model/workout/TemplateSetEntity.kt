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
data class TemplateSetEntity (
    @PrimaryKey(autoGenerate = true) val templateSetId: Long = 0,
    val templateExerciseId: Long,
    val order: Int,
    val type: SetType
)
