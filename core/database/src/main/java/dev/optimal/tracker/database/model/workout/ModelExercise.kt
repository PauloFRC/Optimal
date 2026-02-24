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
            entity = WorkoutModel::class,
            parentColumns = ["workoutModelId"],
            childColumns = ["workoutModelId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["exerciseId"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workoutModelId"), Index("exerciseId")]
)
data class ModelExercise(
    @PrimaryKey(autoGenerate = true) val modelExerciseId: Long = 0,
    val workoutModelId: Long,
    val exerciseId: Long,
    val order: Int
)

data class ModelExerciseWithSets (
    @Embedded val modelExercise: ModelExercise,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "exerciseId"
    )
    val exercise: Exercise,
    @Relation(
        entity = ModelSet::class,
        parentColumn = "modelExerciseId",
        entityColumn = "modelExerciseId"
    )
    val modelSets: List<ModelSet>
)
