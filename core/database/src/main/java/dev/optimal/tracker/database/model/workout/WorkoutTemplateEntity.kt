package dev.optimal.tracker.database.model.workout

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class WorkoutTemplateEntity(
    @PrimaryKey(autoGenerate = true) val workoutTemplateId: Long = 0,
    val name: String,
)

data class WorkoutTemplateWithExercises(
    @Embedded val workoutTemplateEntity: WorkoutTemplateEntity,
    @Relation(
        entity = TemplateExerciseEntity::class,
        parentColumn = "workoutTemplateId",
        entityColumn = "workoutTemplateId",
    )
    val templateExercisesWithSets: List<TemplateExerciseWithSets>
)
