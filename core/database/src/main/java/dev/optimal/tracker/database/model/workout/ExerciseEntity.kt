package dev.optimal.tracker.database.model.workout

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class ExerciseType {
    REPS_WEIGHT,
    REPS,
    ISOMETRIC,
    ISOMETRIC_WEIGHTS
}

@Entity(
    tableName = "exercise",
    foreignKeys = [
        ForeignKey(
            entity = MuscleGroupEntity::class,
            parentColumns = ["muscleGroupId"],
            childColumns = ["primaryMuscleGroupId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("primaryMuscleGroupId")]
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val exerciseId: Long = 0,
    val name: String,
    val primaryMuscleGroupId: Long,
    val description: String = "",
    val unilateral: Boolean = false,
    val type: ExerciseType = ExerciseType.REPS_WEIGHT
    // todo: image
    // todo: perm observation
)
