package dev.optimal.tracker.util

import dev.optimal.tracker.data.local.model.workout.Exercise
import dev.optimal.tracker.data.local.model.workout.ExerciseType
import dev.optimal.tracker.data.local.model.workout.ModelExercise
import dev.optimal.tracker.data.local.model.workout.ModelExerciseWithSets
import dev.optimal.tracker.data.local.model.workout.ModelSet
import dev.optimal.tracker.data.local.model.workout.MuscleGroup
import dev.optimal.tracker.data.local.model.workout.SessionExercise
import dev.optimal.tracker.data.local.model.workout.SessionExerciseWithSets
import dev.optimal.tracker.data.local.model.workout.SessionSet
import dev.optimal.tracker.data.local.model.workout.SetType
import dev.optimal.tracker.data.local.model.workout.WorkoutModel
import dev.optimal.tracker.data.local.model.workout.WorkoutModelWithExercises
import dev.optimal.tracker.data.local.model.workout.WorkoutSession
import dev.optimal.tracker.data.local.model.workout.WorkoutSessionWithExercises
import java.util.Date

object FakeWorkoutFactory {

    // Base Entity Factories

    fun createMuscleGroup(
        muscleGroupId: Long = 1,
        name: String = "Chest"
    ) = MuscleGroup(
        muscleGroupId = muscleGroupId,
        name = name
    )

    fun createExercise(
        exerciseId: Long = 1,
        name: String = "Bench Press",
        description: String = "Compound exercise for the upper body.",
        type: ExerciseType = ExerciseType.REPS_WEIGHT
    ) = Exercise(
        exerciseId = exerciseId,
        name = name,
        description = description,
        type = type
    )

    fun createWorkoutModel(
        workoutModelId: Long = 1,
        name: String = "Push Day"
    ) = WorkoutModel(
        workoutModelId = workoutModelId,
        name = name
    )

    fun createModelExercise(
        modelExerciseId: Long = 1,
        workoutModelId: Long = 1,
        exerciseId: Long = 1,
        order: Int = 0
    ) = ModelExercise(
        modelExerciseId = modelExerciseId,
        workoutModelId = workoutModelId,
        exerciseId = exerciseId,
        order = order
    )

    fun createModelSet(
        modelSetId: Long = 1,
        modelExerciseId: Long = 1,
        order: Int = 0,
        type: SetType = SetType.WORKING
    ) = ModelSet(
        modelSetId = modelSetId,
        modelExerciseId = modelExerciseId,
        order = order,
        type = type
    )

    fun createWorkoutSession(
        workoutSessionId: Long = 1,
        workoutModelId: Long = 1,
        name: String = "Push Day",
        completed: Boolean = true,
        startDate: Date = Date(),
        endDate: Date? = null
    ) = WorkoutSession(
        workoutSessionId = workoutSessionId,
        workoutModelId = workoutModelId,
        name = name,
        completed = completed,
        startDate = startDate,
        endDate = endDate
    )

    fun createSessionExercise(
        sessionExerciseId: Long = 1,
        workoutSessionId: Long = 1,
        exerciseId: Long = 1,
        order: Int = 0
    ) = SessionExercise(
        sessionExerciseId = sessionExerciseId,
        workoutSessionId = workoutSessionId,
        exerciseId = exerciseId,
        order = order
    )

    fun createSessionSet(
        sessionSetId: Long = 1,
        sessionExerciseId: Long = 1,
        order: Int = 0,
        type: SetType = SetType.WORKING,
        completed: Boolean = true,
        reps: Int? = 8,
        weight: Double? = 60.0,
        rir: Int? = 2
    ) = SessionSet(
        sessionSetId = sessionSetId,
        sessionExerciseId = sessionExerciseId,
        order = order,
        type = type,
        completed = completed,
        reps = reps,
        weight = weight,
        rir = rir
    )

    // Composite Object Factories

    fun createModelExerciseWithSets(
        modelExercise: ModelExercise = createModelExercise(),
        exercise: Exercise = createExercise(exerciseId = modelExercise.exerciseId),
        setCount: Int = 3,
        baseId: Int = 0, // to avoid insertions in the same id
    ): ModelExerciseWithSets {
        val modelSets = (1..setCount).map {
            createModelSet(modelSetId = (it + baseId).toLong(), modelExerciseId = modelExercise.modelExerciseId, order = it - 1)
        }
        return ModelExerciseWithSets(
            modelExercise = modelExercise,
            exercise = exercise,
            modelSets = modelSets
        )
    }

    fun createWorkoutModelWithExercises(
        workoutModel: WorkoutModel = createWorkoutModel(),
        exerciseCount: Int = 2,
        setCount: Int = 3,
    ): WorkoutModelWithExercises {
        val modelExercisesWithSets = (1..exerciseCount).map {
            val exercise = createExercise(exerciseId = it.toLong(), name = "Exercise $it")
            val modelExercise = createModelExercise(
                modelExerciseId = it.toLong(),
                workoutModelId = workoutModel.workoutModelId,
                exerciseId = exercise.exerciseId,
                order = it - 1
            )
            createModelExerciseWithSets(
                modelExercise = modelExercise,
                exercise = exercise,
                setCount = setCount,
                baseId = (it - 1) * setCount
            )
        }
        return WorkoutModelWithExercises(
            workoutModel = workoutModel,
            modelExercisesWithSets = modelExercisesWithSets
        )
    }

    fun createSessionExerciseWithSets(
        sessionExercise: SessionExercise = createSessionExercise(),
        exercise: Exercise = createExercise(exerciseId = sessionExercise.exerciseId),
        setCount: Int = 3
    ): SessionExerciseWithSets {
        val sessionSets = (1..setCount).map {
            createSessionSet(sessionExerciseId = sessionExercise.sessionExerciseId, order = it - 1)
        }
        return SessionExerciseWithSets(
            sessionExercise = sessionExercise,
            exercise = exercise,
            sessionSets = sessionSets
        )
    }

    fun createWorkoutSessionWithExercises(
        workoutSession: WorkoutSession = createWorkoutSession(),
        exerciseCount: Int = 2
    ): WorkoutSessionWithExercises {
        val sessionExercisesWithSets = (1..exerciseCount).map {
            val exercise = createExercise(exerciseId = it.toLong(), name = "Exercise $it")
            val sessionExercise = createSessionExercise(
                sessionExerciseId = it.toLong(),
                workoutSessionId = workoutSession.workoutSessionId,
                exerciseId = exercise.exerciseId,
                order = it - 1
            )
            createSessionExerciseWithSets(sessionExercise = sessionExercise, exercise = exercise)
        }
        return WorkoutSessionWithExercises(
            workoutSession = workoutSession,
            sessionExercisesWithSets = sessionExercisesWithSets
        )
    }
}
