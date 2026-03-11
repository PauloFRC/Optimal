package dev.optimal.tracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.optimal.tracker.database.dao.ExerciseDao
import dev.optimal.tracker.database.dao.WorkoutModelDao
import dev.optimal.tracker.database.dao.WorkoutSessionDao
import dev.optimal.tracker.database.model.workout.ExerciseEntity
import dev.optimal.tracker.database.model.workout.ExerciseSecondaryMuscleGroupCrossRef
import dev.optimal.tracker.database.model.workout.ModelExerciseEntity
import dev.optimal.tracker.database.model.workout.ModelSetEntity
import dev.optimal.tracker.database.model.workout.MuscleGroupEntity
import dev.optimal.tracker.database.model.workout.SessionExerciseEntity
import dev.optimal.tracker.database.model.workout.SessionSetEntity
import dev.optimal.tracker.database.model.workout.WorkoutModelEntity
import dev.optimal.tracker.database.model.workout.WorkoutSessionEntity
import dev.optimal.tracker.database.model.workout.converters.DateTimeConverter
import dev.optimal.tracker.database.model.workout.converters.ExerciseTypeConverter
import dev.optimal.tracker.database.model.workout.converters.SetTypeConverter

@Database(
    entities = [
        ExerciseEntity::class,
        ModelExerciseEntity::class,
        SessionExerciseEntity::class,
        ExerciseSecondaryMuscleGroupCrossRef::class,
        MuscleGroupEntity::class,
        ModelSetEntity::class,
        SessionSetEntity::class,
        WorkoutModelEntity::class,
        WorkoutSessionEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateTimeConverter::class,
    ExerciseTypeConverter::class,
    SetTypeConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutModelDao(): WorkoutModelDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
}
