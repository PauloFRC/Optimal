package dev.optimal.tracker.database.model.workout

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["exerciseId", "secondaryMuscleGroupId"],
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["exerciseId"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MuscleGroupEntity::class,
            parentColumns = ["muscleGroupId"],
            childColumns = ["secondaryMuscleGroupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("exerciseId"), Index("secondaryMuscleGroupId")]
)
data class ExerciseSecondaryMuscleGroupCrossRef(
    val exerciseId: Long,
    val secondaryMuscleGroupId: Long,
)
