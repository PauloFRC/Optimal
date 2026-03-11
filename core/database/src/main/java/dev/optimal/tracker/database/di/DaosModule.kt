package dev.optimal.tracker.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.optimal.tracker.database.AppDatabase
import dev.optimal.tracker.database.dao.ExerciseDao
import dev.optimal.tracker.database.dao.WorkoutModelDao
import dev.optimal.tracker.database.dao.WorkoutSessionDao

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesExerciseDao(
        database: AppDatabase,
    ): ExerciseDao = database.exerciseDao()

    @Provides
    fun providesWorkoutModelDao(
        database: AppDatabase,
    ): WorkoutModelDao = database.workoutModelDao()

    @Provides
    fun providesWorkoutSessionDao(
        database: AppDatabase,
    ): WorkoutSessionDao = database.workoutSessionDao()
}
