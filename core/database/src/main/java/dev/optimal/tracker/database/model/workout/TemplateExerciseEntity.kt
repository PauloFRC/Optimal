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
            entity = WorkoutTemplateEntity::class,
            parentColumns = ["workoutTemplateId"],
            childColumns = ["workoutTemplateId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["exerciseId"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workoutTemplateId"), Index("exerciseId")]
)
data class TemplateExerciseEntity(
    @PrimaryKey(autoGenerate = true) val templateExerciseId: Long = 0,
    val workoutTemplateId: Long,
    val exerciseId: Long,
    val order: Int
)

data class TemplateExerciseWithSets (
    @Embedded val templateExerciseEntity: TemplateExerciseEntity,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "exerciseId"
    )
    val exerciseEntity: ExerciseEntity,
    @Relation(
        entity = TemplateSetEntity::class,
        parentColumn = "templateExerciseId",
        entityColumn = "templateExerciseId"
    )
    val templateSetEntities: List<TemplateSetEntity>
)
