package dev.optimal.tracker.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.optimal.tracker.database.AppDatabase
import org.junit.After
import org.junit.Before

internal abstract class DatabaseTest {

    private lateinit var db: AppDatabase
    protected lateinit var exerciseDao: ExerciseDao
    protected lateinit var workoutModelDao: WorkoutModelDao
    protected lateinit var workoutSessionDao: WorkoutSessionDao

    @Before
    fun setup() {
        db = run {
            val context = ApplicationProvider.getApplicationContext<Context>()
            Room.inMemoryDatabaseBuilder(
                context,
                AppDatabase::class.java,
            ).build()
        }
        exerciseDao = db.exerciseDao()
        workoutModelDao = db.workoutModelDao()
        workoutSessionDao = db.workoutSessionDao()
    }

    @After
    fun teardown() = db.close()
}
