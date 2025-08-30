package dev.optimal.tracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.optimal.tracker.data.local.dao.ExerciseDao
import dev.optimal.tracker.data.local.dao.WorkoutModelDao
import dev.optimal.tracker.data.local.dao.WorkoutSessionDao
import dev.optimal.tracker.data.local.model.workout.Exercise
import dev.optimal.tracker.data.local.model.workout.ExerciseMuscleGroupCrossRef
import dev.optimal.tracker.data.local.model.workout.ModelExercise
import dev.optimal.tracker.data.local.model.workout.ModelSet
import dev.optimal.tracker.data.local.model.workout.MuscleGroup
import dev.optimal.tracker.data.local.model.workout.SessionExercise
import dev.optimal.tracker.data.local.model.workout.SessionSet
import dev.optimal.tracker.data.local.model.workout.WorkoutModel
import dev.optimal.tracker.data.local.model.workout.WorkoutSession

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

    abstract fun workoutDao(): WorkoutModelDao

    abstract fun workoutSessionDao(): WorkoutSessionDao
}