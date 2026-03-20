package dev.optimal.tracker.database.dao

import dev.optimal.tracker.database.model.workout.ExerciseEntity
import dev.optimal.tracker.model.workout.enums.ExerciseType
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class ExerciseDaoTest : DatabaseTest() {

    @Test
    fun getAllExercises_areOrderedByNameAsc() = runTest {
        val exercises = listOf(
            testExerciseEntity(id = 1, name = "Squat"),
            testExerciseEntity(id = 2, name = "Bench Press"),
            testExerciseEntity(id = 3, name = "Deadlift")
        )
        exercises.forEach { exerciseDao.insertExercise(it) }

        val savedExercises = exerciseDao.getAllExercises()

        assertEquals(
            listOf("Bench Press", "Deadlift", "Squat"),
            savedExercises.map { it.name }
        )
    }

    @Test
    fun searchExercisesByName_returnsMatchingEntries() = runTest {
        val exercises = listOf(
            testExerciseEntity(id = 1, name = "Barbell Bench Press"),
            testExerciseEntity(id = 2, name = "Dumbbell Bench Press"),
            testExerciseEntity(id = 3, name = "Squat")
        )
        exercises.forEach { exerciseDao.insertExercise(it) }

        val searchResults = exerciseDao.searchExercisesByName("Bench")

        assertEquals(2, searchResults.size)
    }
}

fun testExerciseEntity(
    id: Long = 0,
    name: String,
    type: ExerciseType = ExerciseType.REPS_WEIGHT
) = ExerciseEntity(
    exerciseId = id,
    name = name,
    primaryMuscleGroupId = null,
    description = "",
    unilateral = false,
    type = type
)
