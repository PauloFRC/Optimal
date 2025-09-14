package dev.optimal.tracker.data.local.model.workout

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import dev.optimal.tracker.data.local.converter.DateConverter
import java.util.Date

@Entity
data class WorkoutModel(
    @PrimaryKey(autoGenerate = true) val workoutModelId: Long = 0,
    val name: String,
)

@Entity
@TypeConverters(DateConverter::class)
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true) val workoutSessionId: Long = 0,
    val workoutModelId: Long,
    val name: String,
    val completed: Boolean,
    val startDate: Date,
    val endDate: Date?
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

data class WorkoutSessionWithExercises(
    @Embedded val workoutSession: WorkoutSession,

    @Relation(
        entity = SessionExercise::class,
        parentColumn = "workoutSessionId",
        entityColumn = "workoutSessionId"
    )
    val sessionExercisesWithSets: List<SessionExerciseWithSets>
)

