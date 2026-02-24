package dev.optimal.tracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.optimal.tracker.database.dao.ExerciseDao
import dev.optimal.tracker.database.dao.WorkoutModelDao
import dev.optimal.tracker.database.dao.WorkoutSessionDao
import dev.optimal.tracker.database.model.workout.Exercise
import dev.optimal.tracker.database.model.workout.ExerciseMuscleGroupCrossRef
import dev.optimal.tracker.database.model.workout.ModelExercise
import dev.optimal.tracker.database.model.workout.ModelSet
import dev.optimal.tracker.database.model.workout.MuscleGroup
import dev.optimal.tracker.database.model.workout.SessionExercise
import dev.optimal.tracker.database.model.workout.SessionSet
import dev.optimal.tracker.database.model.workout.WorkoutModel
import dev.optimal.tracker.database.model.workout.WorkoutSession

@Database(
    entities = [
        Exercise::class,
        ModelExercise::class,
        SessionExercise::class,
        ExerciseMuscleGroupCrossRef::class,
        MuscleGroup::class,
        ModelSet::class,
        SessionSet::class,
        WorkoutModel::class,
        WorkoutSession::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutModelDao(): WorkoutModelDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
}
