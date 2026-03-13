package dev.optimal.tracker.data.model

import dev.optimal.tracker.database.model.workout.SessionExerciseWithSets
import dev.optimal.tracker.database.model.workout.SessionSetEntity
import dev.optimal.tracker.database.model.workout.WorkoutSessionWithExercises
import dev.optimal.tracker.model.workout.SessionExerciseModel
import dev.optimal.tracker.model.workout.SessionSetModel
import dev.optimal.tracker.model.workout.WorkoutSessionModel

fun WorkoutSessionWithExercises.toModel() = WorkoutSessionModel(
    id = workoutSession.workoutSessionId,
    workoutModelId = workoutSession.workoutModelId,
    name = workoutSession.name,
    isCompleted = workoutSession.completed,
    startDate = workoutSession.startDate,
    endDate = workoutSession.endDate,
    exercises = exercises
        .sortedBy { it.sessionExercise.order }
        .map { it.toModel() }
)

fun SessionExerciseWithSets.toModel() = SessionExerciseModel(
    id = sessionExercise.sessionExerciseId,
    name = exercise.name,
    sets = sessionSets
        .sortedBy { it.order }
        .map { it.toModel() }
)

fun SessionSetEntity.toModel() = SessionSetModel(
    id = sessionSetId,
    order = order,
    type = type,
    isCompleted = completed,
    reps = reps,
    weight = weight,
    rir = rir
)
