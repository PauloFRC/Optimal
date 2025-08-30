package dev.optimal.tracker.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import dev.optimal.tracker.data.local.AppDatabase
import dev.optimal.tracker.util.FakeWorkoutFactory
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

import com.google.common.truth.Truth.assertThat

@RunWith(AndroidJUnit4::class)
@SmallTest
class WorkoutDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var workoutDao: WorkoutModelDao
    private lateinit var exerciseDao: ExerciseDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries().build()
        workoutDao = database.workoutDao()
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
        val insertedId = workoutDao.insertWorkoutModel(workoutModel)
        val retrieved = workoutDao.getWorkoutModelById(insertedId)

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
        workoutDao.insertWorkoutModel(workout1)
        workoutDao.insertWorkoutModel(workout2)
        workoutDao.insertWorkoutModel(workout3)

        val allWorkouts = workoutDao.getAllWorkoutModels()

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

        workoutDao.insertWorkoutModel(pushWorkout)
        workoutDao.insertWorkoutModel(pullWorkout)
        workoutDao.insertWorkoutModel(legWorkout)

        // When
        val dayWorkouts = workoutDao.searchWorkoutModelsByName("Day")
        val pushWorkouts = workoutDao.searchWorkoutModelsByName("Push")

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
        val insertedId = workoutDao.insertWorkoutModel(originalWorkout)

        // When
        val updatedWorkout = FakeWorkoutFactory.createWorkoutModel(
            workoutModelId = insertedId,
            name = "Updated Name",
        )
        workoutDao.updateWorkoutModel(updatedWorkout)

        // Then
        val retrieved = workoutDao.getWorkoutModelById(insertedId)
        assertThat(retrieved?.name).isEqualTo("Updated Name")
    }

    @Test
    fun givenInsertedWorkoutModelWhenDeletedThenItIsRemoved() = runTest {
        // Given
        val workout = FakeWorkoutFactory.createWorkoutModel(0, "To Delete")
        val insertedId = workoutDao.insertWorkoutModel(workout)

        // When
        workoutDao.deleteWorkoutModelById(insertedId)

        // Then
        val retrieved = workoutDao.getWorkoutModelById(insertedId)
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

        val workoutModelId = workoutDao.insertWorkoutModel(workoutModel)
        modelExercises.forEach { modelExerciseWithSets ->
            exerciseDao.insertExercise(modelExerciseWithSets.exercise)
            workoutDao.insertModelExercise(modelExerciseWithSets.modelExercise)
            modelExerciseWithSets.modelSets.forEach {
                workoutDao.insertModelSet(it)
            }
        }

        // When
        val dbWorkoutModelWithExercises =
            workoutDao.getWorkoutModelWithExercises(workoutModelId)

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
    fun insertCompleteWorkoutModel_createsAllRelatedData() = runTest {
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
        val workoutModelId = workoutDao.insertCompleteWorkoutModel(
            workoutModel, modelExercises, modelSets
        )

        // Then
        val retrieved = workoutDao.getWorkoutModelWithExercises(workoutModelId)
        assertThat(retrieved).isNotNull()
        assertThat(retrieved?.modelExercisesWithSets).hasSize(modelExercisesWithSets.size)

        val totalSets = retrieved?.modelExercisesWithSets?.sumOf { it.modelSets.size } ?: 0
        assertThat(totalSets).isEqualTo(modelSets.size)
    }
}

// TODO: fazer testes sessão, melhorar logica de testes, talvez uma função para ajudar na inserção de tudo sem problemas
// TODO: também revisar as variavies para ver se os nomes estão corretos

// Create this in src/androidTest/java/your/package/dao/
//@RunWith(AndroidJUnit4::class)
//class WorkoutSessionDaoTest {
//
//    private lateinit var database: YourDatabase
//    private lateinit var workoutSessionDao: WorkoutSessionDao
//
//    @Before
//    fun createDb() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        database = Room.inMemoryDatabaseBuilder(
//            context, YourDatabase::class.java
//        ).build()
//        workoutSessionDao = database.workoutSessionDao()
//    }
//
//    @After
//    fun closeDb() {
//        database.close()
//    }
//
//    @Test
//    fun insertAndGetWorkoutSession() = runTest {
//        // Given
//        val session = WorkoutSession(
//            workoutSessionId = 0,
//            workoutModelId = 1,
//            name = "Monday Workout",
//            startDate = Date(),
//            endDate = null,
//            notes = "Great session"
//        )
//
//        // When
//        val sessionId = workoutSessionDao.insertWorkoutSession(session)
//        val retrieved = workoutSessionDao.getWorkoutSessionById(sessionId.toInt())
//
//        // Then
//        assertThat(retrieved).isNotNull()
//        assertThat(retrieved?.name).isEqualTo("Monday Workout")
//        assertThat(retrieved?.workoutModelId).isEqualTo(1)
//    }
//
//    @Test
//    fun getAllWorkoutSessions_orderedByDateDescending() = runTest {
//        // Given
//        val calendar = Calendar.getInstance()
//
//        val session1 = WorkoutSession(0, 1, "Oldest", calendar.apply {
//            set(2024, 0, 1)
//        }.time, null, "")
//
//        val session2 = WorkoutSession(0, 1, "Newest", calendar.apply {
//            set(2024, 0, 3)
//        }.time, null, "")
//
//        val session3 = WorkoutSession(0, 1, "Middle", calendar.apply {
//            set(2024, 0, 2)
//        }.time, null, "")
//
//        // When
//        workoutSessionDao.insertWorkoutSession(session1)
//        workoutSessionDao.insertWorkoutSession(session2)
//        workoutSessionDao.insertWorkoutSession(session3)
//
//        val sessions = workoutSessionDao.getAllWorkoutSessions()
//
//        // Then
//        assertThat(sessions).hasSize(3)
//        assertThat(sessions[0].name).isEqualTo("Newest")
//        assertThat(sessions[1].name).isEqualTo("Middle")
//        assertThat(sessions[2].name).isEqualTo("Oldest")
//    }
//
//    @Test
//    fun getWorkoutSessionsByDateRange_filtersCorrectly() = runTest {
//        // Given
//        val calendar = Calendar.getInstance()
//
//        val jan1 = calendar.apply { set(2024, 0, 1) }.time
//        val jan15 = calendar.apply { set(2024, 0, 15) }.time
//        val jan31 = calendar.apply { set(2024, 0, 31) }.time
//        val feb15 = calendar.apply { set(2024, 1, 15) }.time
//
//        workoutSessionDao.insertWorkoutSession(WorkoutSession(0, 1, "Jan 1", jan1, null, ""))
//        workoutSessionDao.insertWorkoutSession(WorkoutSession(0, 1, "Jan 15", jan15, null, ""))
//        workoutSessionDao.insertWorkoutSession(WorkoutSession(0, 1, "Jan 31", jan31, null, ""))
//        workoutSessionDao.insertWorkoutSession(WorkoutSession(0, 1, "Feb 15", feb15, null, ""))
//
//        // When
//        val januarySessions = workoutSessionDao.getWorkoutSessionsByDateRange(jan1, jan31)
//
//        // Then
//        assertThat(januarySessions).hasSize(3)
//        assertThat(januarySessions.map { it.workoutSession.name })
//            .containsExactly("Jan 31", "Jan 15", "Jan 1") // Ordered by date DESC
//    }
//
//    @Test
//    fun getRecentWorkoutSessions_limitsResults() = runTest {
//        // Given - Insert 5 sessions
//        repeat(5) { i ->
//            val session = WorkoutSession(0, 1, "Session $i", Date(), null, "")
//            workoutSessionDao.insertWorkoutSession(session)
//        }
//
//        // When
//        val recentThree = workoutSessionDao.getRecentWorkoutSessions(3)
//
//        // Then
//        assertThat(recentThree).hasSize(3)
//    }
//
//    @Test
//    fun updateSessionSetPerformance_changesData() = runTest {
//        // Given
//        val session = WorkoutSession(0, 1, "Test Session", Date(), null, "")
//        val sessionId = workoutSessionDao.insertWorkoutSession(session).toInt()
//
//        val exercise = SessionExercise(0, sessionId, 1, 1, "Bench Press")
//        val exerciseId = workoutSessionDao.insertSessionExercise(exercise)
//
//        val set = SessionSet(0, exerciseId, 1, 10, 100.0, 2, false)
//        val setId = workoutSessionDao.insertSessionSet(set)
//
//        // When
//        workoutSessionDao.updateSessionSetPerformance(setId, 12, 110.0, 1)
//
//        // Then
//        val updatedSession = workoutSessionDao.getWorkoutSessionWithExercises(sessionId)
//        val updatedSet = updatedSession?.sessionExercises?.first()?.sessionSets?.first()
//
//        assertThat(updatedSet?.reps).isEqualTo(12)
//        assertThat(updatedSet?.weight).isEqualTo(110.0)
//        assertThat(updatedSet?.rir).isEqualTo(1)
//    }
//
//    @Test
//    fun markSessionSetCompleted_setsCompletedToTrue() = runTest {
//        // Given
//        val session = WorkoutSession(0, 1, "Test Session", Date(), null, "")
//        val sessionId = workoutSessionDao.insertWorkoutSession(session).toInt()
//
//        val exercise = SessionExercise(0, sessionId, 1, 1, "Bench Press")
//        val exerciseId = workoutSessionDao.insertSessionExercise(exercise)
//
//        val set = SessionSet(0, exerciseId, 1, 10, 100.0, 2, false)
//        val setId = workoutSessionDao.insertSessionSet(set)
//
//        // When
//        workoutSessionDao.markSessionSetCompleted(setId)
//
//        // Then
//        val updatedSession = workoutSessionDao.getWorkoutSessionWithExercises(sessionId)
//        val updatedSet = updatedSession?.sessionExercises?.first()?.sessionSets?.first()
//
//        assertThat(updatedSet?.completed).isTrue()
//    }
//
//    @Test
//    fun insertCompleteWorkoutSession_createsAllRelatedData() = runTest {
//        // Given
//        val session = WorkoutSession(0, 1, "Complete Session", Date(), null, "Full test")
//
//        val exercises = listOf(
//            SessionExercise(0, 0, 1, 1, "Bench Press"),
//            SessionExercise(0, 0, 2, 2, "Squats")
//        )
//
//        val sets = listOf(
//            SessionSet(0, 0, 1, 10, 100.0, 2, false), // Will be linked to first exercise
//            SessionSet(0, 0, 2, 8, 105.0, 1, false),  // Will be linked to first exercise
//            SessionSet(0, 1, 1, 12, 150.0, 2, false)  // Will be linked to second exercise
//        )
//
//        // When
//        val sessionId = workoutSessionDao.insertCompleteWorkoutSession(session, exercises, sets)
//
//        // Then
//        val retrieved = workoutSessionDao.getWorkoutSessionWithExercises(sessionId.toInt())
//        assertThat(retrieved).isNotNull()
//        assertThat(retrieved?.sessionExercises).hasSize(2)
//
//        val totalSets = retrieved?.sessionExercises?.sumOf { it.sessionSets.size } ?: 0
//        assertThat(totalSets).isEqualTo(3)
//    }
//}

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