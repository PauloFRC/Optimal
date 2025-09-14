package dev.optimal.tracker.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dev.optimal.tracker.data.local.AppDatabase
import dev.optimal.tracker.util.FakeWorkoutFactory
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class WorkoutModelDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var workoutModelDao: WorkoutModelDao
    private lateinit var exerciseDao: ExerciseDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries().build()
        workoutModelDao = database.workoutModelDao()
        exerciseDao = database.exerciseDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun givenWorkoutModelWhenInsertedThenItCanBeRetrieved() = runTest {
        // Given
        val workoutModel = FakeWorkoutFactory.createWorkoutModel()

        // When
        val insertedId = workoutModelDao.insertWorkoutModel(workoutModel)
        val retrieved = workoutModelDao.getWorkoutModelById(insertedId)

        // Then
        assertNotNull(insertedId)
        assertEquals(retrieved?.workoutModelId, insertedId)
        assertEquals(retrieved?.name, workoutModel.name)
    }

    @Test
    fun givenInsertedWorkoutModelsWhenSearchedThenRetrieveAlphabetically() = runTest {
        // Given
        val workout1 = FakeWorkoutFactory.createWorkoutModel(1, "A")
        val workout2 = FakeWorkoutFactory.createWorkoutModel(2, "B")
        val workout3 = FakeWorkoutFactory.createWorkoutModel(3, "C")

        // When
        workoutModelDao.insertWorkoutModel(workout1)
        workoutModelDao.insertWorkoutModel(workout2)
        workoutModelDao.insertWorkoutModel(workout3)

        val allWorkouts = workoutModelDao.getAllWorkoutModels()

        // Then
        assertThat(allWorkouts).hasSize(3)
        assertThat(allWorkouts[0].name).isEqualTo("A")
        assertThat(allWorkouts[1].name).isEqualTo("B")
        assertThat(allWorkouts[2].name).isEqualTo("C")
    }

    @Test
    fun givenInsertedWorkoutModelsWhenSearchedThenRetrieveMatching() = runTest {
        // Given
        val pushWorkout = FakeWorkoutFactory.createWorkoutModel(1, "Push Day")
        val pullWorkout = FakeWorkoutFactory.createWorkoutModel(2, "Pull Day")
        val legWorkout = FakeWorkoutFactory.createWorkoutModel(3, "Leg Day")

        workoutModelDao.insertWorkoutModel(pushWorkout)
        workoutModelDao.insertWorkoutModel(pullWorkout)
        workoutModelDao.insertWorkoutModel(legWorkout)

        // When
        val dayWorkouts = workoutModelDao.searchWorkoutModelsByName("Day")
        val pushWorkouts = workoutModelDao.searchWorkoutModelsByName("Push")

        // Then
        assertThat(dayWorkouts).hasSize(3) // All contain "Day"
        assertThat(pushWorkouts).hasSize(1) // Only push workout
        assertThat(pushWorkouts[0].workoutModel.name).isEqualTo("Push Day")
    }

    @Test
    fun givenInsertedWorkoutModelWhenUpdatedThenItIsChanged() = runTest {
        // Given
        val originalWorkout = FakeWorkoutFactory.createWorkoutModel(
            workoutModelId = 1,
            name = "Original Name"
        )
        val insertedId = workoutModelDao.insertWorkoutModel(originalWorkout)

        // When
        val updatedWorkout = FakeWorkoutFactory.createWorkoutModel(
            workoutModelId = insertedId,
            name = "Updated Name",
        )
        workoutModelDao.updateWorkoutModel(updatedWorkout)

        // Then
        val retrieved = workoutModelDao.getWorkoutModelById(insertedId)
        assertThat(retrieved?.name).isEqualTo("Updated Name")
    }

    @Test
    fun givenInsertedWorkoutModelWhenDeletedThenItIsRemoved() = runTest {
        // Given
        val workout = FakeWorkoutFactory.createWorkoutModel(0, "To Delete")
        val insertedId = workoutModelDao.insertWorkoutModel(workout)

        // When
        workoutModelDao.deleteWorkoutModelById(insertedId)

        // Then
        val retrieved = workoutModelDao.getWorkoutModelById(insertedId)
        assertThat(retrieved).isNull()
    }

    @Test
    fun givenWorkoutModelWithExercisesWhenRetrievedThenIncludesAll() = runTest {
        // Given - Create a workout model with exercises and sets
        val workoutModelWithExercises = FakeWorkoutFactory.createWorkoutModelWithExercises(
            exerciseCount = 2,
            setCount = 3
        )
        val workoutModel = workoutModelWithExercises.workoutModel
        val modelExercises = workoutModelWithExercises.modelExercisesWithSets

        val workoutModelId = workoutModelDao.insertWorkoutModel(workoutModel)
        modelExercises.forEach { modelExerciseWithSets ->
            exerciseDao.insertExercise(modelExerciseWithSets.exercise)
            workoutModelDao.insertModelExercise(modelExerciseWithSets.modelExercise)
            modelExerciseWithSets.modelSets.forEach {
                workoutModelDao.insertModelSet(it)
            }
        }

        // When
        val dbWorkoutModelWithExercises =
            workoutModelDao.getWorkoutModelWithExercises(workoutModelId)

        // Then
        assertThat(dbWorkoutModelWithExercises).isNotNull()
        assertThat(dbWorkoutModelWithExercises?.workoutModel?.name).isEqualTo(workoutModel.name)
        assertThat(dbWorkoutModelWithExercises?.modelExercisesWithSets).hasSize(modelExercises.size)

        val firstModelExercise = dbWorkoutModelWithExercises?.modelExercisesWithSets?.find {
            it.modelExercise.order == 0
        }
        assertThat(firstModelExercise?.modelSets).hasSize(
            modelExercises[0].modelSets.size
        )

        val secondModelExercise = dbWorkoutModelWithExercises?.modelExercisesWithSets?.find {
            it.modelExercise.order == 1
        }
        assertThat(secondModelExercise?.modelSets).hasSize(
            modelExercises[1].modelSets.size
        )
    }

    @Test
    fun givenInsertedWorkoutModelWithExercisesWhenRetrievedThenIncludesAll() = runTest {
        // Given
        val workoutModelWithExercises = FakeWorkoutFactory.createWorkoutModelWithExercises(
            exerciseCount = 2,
            setCount = 3
        )
        val workoutModel = workoutModelWithExercises.workoutModel
        val modelExercisesWithSets = workoutModelWithExercises.modelExercisesWithSets
        val modelExercises = modelExercisesWithSets.map { it.modelExercise }
        val modelSets = workoutModelWithExercises.modelExercisesWithSets.flatMap { it.modelSets }
        modelExercisesWithSets.forEach {
            exerciseDao.insertExercise(it.exercise)
        }

        // When
        val workoutModelId = workoutModelDao.insertCompleteWorkoutModel(
            workoutModel, modelExercises, modelSets
        )

        // Then
        val retrieved = workoutModelDao.getWorkoutModelWithExercises(workoutModelId)
        assertThat(retrieved).isNotNull()
        assertThat(retrieved?.modelExercisesWithSets).hasSize(modelExercisesWithSets.size)

        val totalSets = retrieved?.modelExercisesWithSets?.sumOf { it.modelSets.size } ?: 0
        assertThat(totalSets).isEqualTo(modelSets.size)
    }
}

// Example of testing with a custom TestRule for database setup
//class DatabaseRule : TestWatcher() {
//    lateinit var database: YourDatabase
//
//    override fun starting(description: Description?) {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        database = Room.inMemoryDatabaseBuilder(context, YourDatabase::class.java).build()
//    }
//
//    override fun finished(description: Description?) {
//        database.close()
//    }
//}