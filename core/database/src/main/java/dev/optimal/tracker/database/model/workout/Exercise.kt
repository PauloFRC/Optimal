package dev.optimal.tracker.database.model.workout

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ExerciseType {
    REPS_WEIGHT,
    REPS,
    ISOMETRIC,
    ISOMETRIC_WEIGHTS
}

@Entity(tableName = "exercise")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val exerciseId: Long = 0,
    val name: String,
    val description: String = "",
    val unilateral: Boolean = false,
    val type: ExerciseType = ExerciseType.REPS_WEIGHT
    // todo: image
    // todo: perm observation
)
