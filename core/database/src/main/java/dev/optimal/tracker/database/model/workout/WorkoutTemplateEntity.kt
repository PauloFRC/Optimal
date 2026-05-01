package dev.optimal.tracker.database.model.workout

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class WorkoutTemplateEntity(
    @PrimaryKey(autoGenerate = true) val workoutModelId: Long = 0,
    val name: String,
)

data class WorkoutTemplateWithExercises(
    @Embedded val workoutTemplateEntity: WorkoutTemplateEntity,
    @Relation(
        entity = ModelExerciseEntity::class,
        parentColumn = "workoutModelId",
        entityColumn = "workoutModelId",
    )
    val templateExercisesWithSets: List<TemplateExerciseWithSets>
)
