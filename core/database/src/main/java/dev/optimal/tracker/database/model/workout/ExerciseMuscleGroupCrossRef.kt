package dev.optimal.tracker.database.model.workout

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.Relation

enum class MuscleGroupRole {
    PRIMARY,
    SECONDARY
}

@Entity(
    primaryKeys = ["exerciseId", "muscleGroupId"],
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["exerciseId"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MuscleGroup::class,
            parentColumns = ["muscleGroupId"],
            childColumns = ["muscleGroupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("exerciseId"), Index("muscleGroupId")]
)
data class ExerciseMuscleGroupCrossRef(
    val exerciseId: Long,
    val muscleGroupId: Long,
    val role: MuscleGroupRole
)

data class ExerciseWithMuscleGroups(
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "exerciseId",
        entity = MuscleGroup::class,
        entityColumn = "muscleGroupId",
        associateBy = Junction(
            value = ExerciseMuscleGroupCrossRef::class,
            parentColumn = "exerciseId",
            entityColumn = "muscleGroupId"
        )
    )
    val muscleGroups: List<MuscleGroup>
)
