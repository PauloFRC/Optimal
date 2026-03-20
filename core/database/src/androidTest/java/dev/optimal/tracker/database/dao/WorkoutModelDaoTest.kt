package dev.optimal.tracker.database.dao

import dev.optimal.tracker.database.model.workout.ModelExerciseEntity
import dev.optimal.tracker.database.model.workout.ModelSetEntity
import dev.optimal.tracker.database.model.workout.WorkoutModelEntity
import dev.optimal.tracker.model.workout.enums.SetType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class WorkoutModelDaoTest : DatabaseTest() {

    @Test
    fun getWorkoutModelWithExercises_returnsCompleteNestedHierarchy() = runTest {
        val exerciseId = exerciseDao.insertExercise(testExerciseEntity(name = "Push Up"))

        val modelId = workoutModelDao.insertWorkoutModel(
            WorkoutModelEntity(name = "Chest Day")
        )

        val modelExerciseId = workoutModelDao.insertModelExercise(
            ModelExerciseEntity(
                workoutModelId = modelId,
                exerciseId = exerciseId,
                order = 1
            )
        )

        workoutModelDao.insertModelSet(
            ModelSetEntity(modelExerciseId = modelExerciseId, order = 1, type = SetType.WORKING)
        )
        workoutModelDao.insertModelSet(
            ModelSetEntity(modelExerciseId = modelExerciseId, order = 2, type = SetType.WORKING)
        )

        val result = workoutModelDao.getWorkoutModelWithExercises(modelId).first()

        assertNotNull(result)
        assertEquals("Chest Day", result.workoutModelEntity.name)
        assertEquals(1, result.modelExercisesWithSets.size)

        val exerciseWithSets = result.modelExercisesWithSets.first()
        assertEquals("Push Up", exerciseWithSets.exerciseEntity.name)
        assertEquals(2, exerciseWithSets.modelSetEntities.size)
    }
}
