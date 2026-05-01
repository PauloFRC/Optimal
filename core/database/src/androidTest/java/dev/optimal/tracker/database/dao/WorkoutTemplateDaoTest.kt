package dev.optimal.tracker.database.dao

import dev.optimal.tracker.database.model.workout.ModelExerciseEntity
import dev.optimal.tracker.database.model.workout.TemplateSetEntity
import dev.optimal.tracker.database.model.workout.WorkoutTemplateEntity
import dev.optimal.tracker.model.workout.enums.SetType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class WorkoutTemplateDaoTest : DatabaseTest() {

    @Test
    fun getWorkoutTemplateWithExercises_returnsCompleteNestedHierarchy() = runTest {
        val exerciseId = exerciseDao.insertExercise(testExerciseEntity(name = "Push Up"))

        val modelId = workoutTemplateDao.insertWorkoutTemplate(
            WorkoutTemplateEntity(name = "Chest Day")
        )

        val modelExerciseId = workoutTemplateDao.insertTemplateExercise(
            ModelExerciseEntity(
                workoutModelId = modelId,
                exerciseId = exerciseId,
                order = 1
            )
        )

        workoutTemplateDao.insertTemplateSet(
            TemplateSetEntity(templateExerciseId = modelExerciseId, order = 1, type = SetType.WORKING)
        )
        workoutTemplateDao.insertTemplateSet(
            TemplateSetEntity(templateExerciseId = modelExerciseId, order = 2, type = SetType.WORKING)
        )

        val result = workoutTemplateDao.getWorkoutTemplateWithExercises(modelId).first()

        assertNotNull(result)
        assertEquals("Chest Day", result.workoutTemplateEntity.name)
        assertEquals(1, result.templateExercisesWithSets.size)

        val exerciseWithSets = result.templateExercisesWithSets.first()
        assertEquals("Push Up", exerciseWithSets.exerciseEntity.name)
        assertEquals(2, exerciseWithSets.templateSetEntities.size)
    }
}
