package dev.optimal.tracker.data.local.model.workout

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

enum class ExerciseType(val value: Int) {
    REPS_WEIGHT(0),
    REPS(1),
    ISOMETRIC(2),
    ISOMETRIC_WEIGHTS(3);

    companion object {
        fun fromInt(value: Int) = entries.first { it.value == value }
    }
}

enum class MuscleGroupRole {
    PRIMARY,
    SECONDARY
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
    ]
)
data class ModelExercise(
    @PrimaryKey(autoGenerate = true) val modelExerciseId: Long = 0,
    val workoutModelId: Long,
    val exerciseId: Long,
    val order: Int
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSession::class,
            parentColumns = ["workoutSessionId"],
            childColumns = ["workoutSessionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["exerciseId"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SessionExercise(
    @PrimaryKey(autoGenerate = true) val sessionExerciseId: Long = 0,
    val workoutSessionId: Long,
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

data class SessionExerciseWithSets (
    @Embedded val sessionExercise: SessionExercise,

    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "exerciseId"
    )
    val exercise: Exercise,

    @Relation(
        entity = SessionSet::class,
        parentColumn = "sessionExerciseId",
        entityColumn = "sessionExerciseId"
    )
    val sessionSets: List<SessionSet>
)

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