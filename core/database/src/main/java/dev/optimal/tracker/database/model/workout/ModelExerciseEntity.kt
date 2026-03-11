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
            entity = WorkoutModelEntity::class,
            parentColumns = ["workoutModelId"],
            childColumns = ["workoutModelId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["exerciseId"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workoutModelId"), Index("exerciseId")]
)
data class ModelExerciseEntity(
    @PrimaryKey(autoGenerate = true) val modelExerciseId: Long = 0,
    val workoutModelId: Long,
    val exerciseId: Long,
    val order: Int
)

data class ModelExerciseWithSets (
    @Embedded val modelExerciseEntity: ModelExerciseEntity,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "exerciseId"
    )
    val exerciseEntity: ExerciseEntity,
    @Relation(
        entity = ModelSetEntity::class,
        parentColumn = "modelExerciseId",
        entityColumn = "modelExerciseId"
    )
    val modelSetEntities: List<ModelSetEntity>
)
