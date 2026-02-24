package dev.optimal.tracker.database.model.workout

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class WorkoutModel(
    @PrimaryKey(autoGenerate = true) val workoutModelId: Long = 0,
    val name: String,
)

data class WorkoutModelWithExercises(
    @Embedded val workoutModel: WorkoutModel,
    @Relation(
        entity = ModelExercise::class,
        parentColumn = "workoutModelId",
        entityColumn = "workoutModelId",
    )
    val modelExercisesWithSets: List<ModelExerciseWithSets>
)
