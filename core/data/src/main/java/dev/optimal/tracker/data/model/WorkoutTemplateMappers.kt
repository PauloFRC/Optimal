package dev.optimal.tracker.data.model

import dev.optimal.tracker.database.model.workout.TemplateExerciseWithSets
import dev.optimal.tracker.database.model.workout.TemplateSetEntity
import dev.optimal.tracker.database.model.workout.WorkoutTemplateWithExercises
import dev.optimal.tracker.model.workout.TemplateExerciseModel
import dev.optimal.tracker.model.workout.TemplateSetModel
import dev.optimal.tracker.model.workout.WorkoutTemplateModel

fun WorkoutTemplateWithExercises.toModel() = WorkoutTemplateModel(
    id = workoutTemplateEntity.workoutTemplateId,
    name = workoutTemplateEntity.name,
    exercises = templateExercisesWithSets.map { it.toModel() }
)

fun TemplateExerciseWithSets.toModel() = TemplateExerciseModel(
    id = exerciseEntity.exerciseId,
    name = exerciseEntity.name,
    sets = templateSetEntities.map { it.toModel() }
)

fun TemplateSetEntity.toModel() = TemplateSetModel(
    id = templateSetId,
    order = order,
    type = type
)
