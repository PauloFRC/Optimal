package dev.optimal.tracker.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dev.optimal.tracker.data.local.AppDatabase
import dev.optimal.tracker.data.local.model.workout.SessionSet
import dev.optimal.tracker.data.local.model.workout.SetType
import dev.optimal.tracker.data.local.model.workout.WorkoutSession
import dev.optimal.tracker.util.FakeWorkoutFactory
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar
import java.util.Date

@RunWith(AndroidJUnit4::class)
@SmallTest
class WorkoutSessionDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var workoutSessionDao: WorkoutSessionDao
    private lateinit var exerciseDao: ExerciseDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries().build()
        workoutSessionDao = database.workoutSessionDao()
        exerciseDao = database.exerciseDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun givenWorkoutSessionWhenInsertedThenItCanBeRetrieved() = runTest {
        // Given
        val session = FakeWorkoutFactory.createWorkoutSession(
            workoutSessionId = 0,
            workoutModelId = 1,
            name = "Monday Workout",
            startDate = Date(),
            endDate = null,
            completed = true
        )

        // When
        val sessionId = workoutSessionDao.insertWorkoutSession(session)
        val retrieved = workoutSessionDao.getWorkoutSessionById(sessionId)

        // Then
        assertThat(retrieved).isNotNull()
        assertThat(retrieved?.name).isEqualTo("Monday Workout")
        assertThat(retrieved?.workoutModelId).isEqualTo(1)
    }

    @Test
    fun givenWorkoutSessionsWhenRetrievedThenTheyAreOrderedByStartDate() = runTest {
        // Given
        val calendar = Calendar.getInstance()

        val session1 = FakeWorkoutFactory.createWorkoutSession(
            1,
            1,
            "Oldest",
            completed = true,
            calendar.apply {
                set(2025, 0, 1)
            }.time, null)

        val session2 = FakeWorkoutFactory.createWorkoutSession(
            2,
            1,
            "Middle",
            true,
            calendar.apply {
                set(2025, 0, 2)
            }.time, null)

        val session3 = FakeWorkoutFactory.createWorkoutSession(
            3,
            1,
            "Newest",
            completed = true,
            calendar.apply {
                set(2025, 0, 3)
            }.time, null)

        // When
        workoutSessionDao.insertWorkoutSession(session1)
        workoutSessionDao.insertWorkoutSession(session2)
        workoutSessionDao.insertWorkoutSession(session3)

        val sessions = workoutSessionDao.getAllWorkoutSessions()

        // Then
        assertThat(sessions).hasSize(3)
        assertThat(sessions[0].name).isEqualTo("Newest")
        assertThat(sessions[1].name).isEqualTo("Middle")
        assertThat(sessions[2].name).isEqualTo("Oldest")
    }

    @Test
    fun givenInsertedWorkoutSessionsWhenRetrievedByDateRangeThenTheyAreOrdered() = runTest {
        // Given
        val calendar = Calendar.getInstance()

        val jan1 = calendar.apply { set(2024, 0, 1) }.time
        val jan15 = calendar.apply { set(2024, 0, 15) }.time
        val jan31 = calendar.apply { set(2024, 0, 31) }.time
        val feb15 = calendar.apply { set(2024, 1, 15) }.time

        workoutSessionDao.insertWorkoutSession(
            FakeWorkoutFactory.createWorkoutSession(
                0,
                1,
                "Jan 1",
                true,
                jan1,
                null,
            )
        )
        workoutSessionDao.insertWorkoutSession(
            FakeWorkoutFactory.createWorkoutSession(
                0,
                1,
                "Jan 15",
                true,
                jan15,
                null,
            )
        )
        workoutSessionDao.insertWorkoutSession(
            FakeWorkoutFactory.createWorkoutSession(
                0,
                1,
                "Jan 31",
                true,
                jan31,
                null,
            )
        )
        workoutSessionDao.insertWorkoutSession(
            FakeWorkoutFactory.createWorkoutSession(
                0,
                1,
                "Feb 15",
                true,
                feb15,
                null,
            )
        )

        // When
        val januarySessions = workoutSessionDao.getWorkoutSessionsByDateRange(
            jan1,
            jan31
        )

        // Then
        assertThat(januarySessions).hasSize(3)
        assertThat(januarySessions.map { it.workoutSession.name })
            .containsExactly("Jan 31", "Jan 15", "Jan 1") // Ordered by date DESC
    }

    @Test
    fun givenInsertedWorkoutSessionsWhenRetrievedWithLimitThenTheyAreLimited() = runTest {
        // Given - Insert 5 sessions
        repeat(5) { i ->
            val session = FakeWorkoutFactory.createWorkoutSession(
                (i+1).toLong(),
                1,
                "Session $i",
                true,
                Date(),
                null,
            )
            workoutSessionDao.insertWorkoutSession(session)
        }

        // When
        val recentThree = workoutSessionDao.getRecentWorkoutSessions(3)

        // Then
        assertThat(recentThree).hasSize(3)
    }

    @Test
    fun givenWorkoutSessionWithSetsWhenUpdatedThenSetsAreUpdated() = runTest {
        // Given
        val session = FakeWorkoutFactory.createWorkoutSession(
            workoutSessionId = 1,
            workoutModelId = 1,
            name = "Test Session",
            completed = true,
            startDate = Date(),
            endDate = null
        )
        val sessionId = workoutSessionDao.insertWorkoutSession(session)

        val sessionExercise = FakeWorkoutFactory.createSessionExercise(
            0,
            sessionId,
            1,
            1,
        )
        val exerciseId = workoutSessionDao.insertSessionExercise(sessionExercise)

        val set = SessionSet(
            0,
            exerciseId,
            1,
            SetType.WORKING,
            true,
            10,
            100.0,
            2,
        )
        val setId = workoutSessionDao.insertSessionSet(set)

        // When
        workoutSessionDao.updateSessionSetPerformance(setId, 12, 110.0, 1)

        // Then
        val updatedSession = workoutSessionDao.getWorkoutSessionWithExercises(sessionId)
        val updatedSet = updatedSession?.sessionExercisesWithSets?.first()?.sessionSets?.first()

        assertThat(updatedSet?.reps).isEqualTo(12)
        assertThat(updatedSet?.weight).isEqualTo(110.0)
        assertThat(updatedSet?.rir).isEqualTo(1)
    }

    @Test
    fun markSessionSetCompleted_setsCompletedToTrue() = runTest {
        // Given
        val session = WorkoutSession(0, 1, "Test Session", true, Date(), null)
        val sessionId = workoutSessionDao.insertWorkoutSession(session)

        val exercise = FakeWorkoutFactory.createSessionExercise(
            0,
            sessionId,
            1,
            1,
        )
        val exerciseId = workoutSessionDao.insertSessionExercise(exercise)

        val set = SessionSet(
            0,
            exerciseId,
            1,
            SetType.WORKING,
            true,
            10,
            100.0,
            2,
        )
        val setId = workoutSessionDao.insertSessionSet(set)

        // When
        workoutSessionDao.markSessionSetCompleted(setId)

        // Then
        val updatedSession = workoutSessionDao.getWorkoutSessionWithExercises(sessionId)
        val updatedSet = updatedSession?.sessionExercisesWithSets?.first()?.sessionSets?.first()

        assertThat(updatedSet?.completed).isTrue()
    }

    @Test
    fun insertCompleteWorkoutSession_createsAllRelatedData() = runTest {
        // Given
        val session = FakeWorkoutFactory.createWorkoutSession(
            0,
            1,
            "Complete Session",
            true,
            Date(),
            null,
        )

        val exercises = listOf(
            FakeWorkoutFactory.createSessionExercise(0, 0, 1, 1),
            FakeWorkoutFactory.createSessionExercise(0, 0, 2, 2)
        )

        val sets = listOf(
            SessionSet(0, 0, 1, SetType.WORKING, true, 10, 100.0, 2), // Will be linked to first exercise
            SessionSet(0, 0, 2, SetType.WORKING, true, 8, 105.0, 1),  // Will be linked to first exercise
            SessionSet(0, 1, 1, SetType.WORKING, true, 12, 150.0, 2)  // Will be linked to second exercise
        )

        // When
        val sessionId = workoutSessionDao.insertCompleteWorkoutSession(session, exercises, sets)

        // Then
        val retrieved = workoutSessionDao.getWorkoutSessionWithExercises(sessionId)
        assertThat(retrieved).isNotNull()
        assertThat(retrieved?.sessionExercisesWithSets).hasSize(2)

        val totalSets = retrieved?.sessionExercisesWithSets?.sumOf { it.sessionSets.size } ?: 0
        assertThat(totalSets).isEqualTo(3)
    }
}