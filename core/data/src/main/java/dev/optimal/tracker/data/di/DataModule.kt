package dev.optimal.tracker.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.optimal.tracker.data.repository.DefaultWorkoutSessionRepository
import dev.optimal.tracker.data.repository.DefaultWorkoutTemplateRepository
import dev.optimal.tracker.data.repository.WorkoutSessionRepository
import dev.optimal.tracker.data.repository.WorkoutTemplateRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindWorkoutSessionRepository(
        workoutSessionRepository: DefaultWorkoutSessionRepository
    ): WorkoutSessionRepository

    @Binds
    internal abstract fun bindWorkoutTemplateRepository(
        workoutTemplateRepository: DefaultWorkoutTemplateRepository
    ): WorkoutTemplateRepository
}
