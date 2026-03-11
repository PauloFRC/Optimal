package dev.optimal.tracker.database.model.workout

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class WorkoutModelEntity(
    @PrimaryKey(autoGenerate = true) val workoutModelId: Long = 0,
    val name: String,
)

data class WorkoutModelWithExercises(
    @Embedded val workoutModelEntity: WorkoutModelEntity,
    @Relation(
        entity = ModelExerciseEntity::class,
        parentColumn = "workoutModelId",
        entityColumn = "workoutModelId",
    )
    val modelExercisesWithSets: List<ModelExerciseWithSets>
)
