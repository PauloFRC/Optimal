package dev.optimal.tracker.home

import android.os.Build
import androidx.annotation.RequiresApi
import dev.optimal.tracker.model.workout.SessionExerciseModel
import dev.optimal.tracker.model.workout.WorkoutSessionModel
import java.time.LocalDateTime

data class HomeState @RequiresApi(Build.VERSION_CODES.O) constructor(
//    val sessionHistory: List<WorkoutSessionModel> = listOf()
    val sessionHistory: List<WorkoutSessionModel> = List(30) {
        WorkoutSessionModel(
            id = 1,
            workoutModelId = 1,
            name = "Session Name",
            isCompleted = true,
            startDate = LocalDateTime.of(2026, 3, 19, 14, 30),
            endDate = LocalDateTime.of(2026, 3, 19, 17, 15),  // Fixed: different end time
            exercises = listOf(
                SessionExerciseModel(1, "Bench Press", listOf()),
                SessionExerciseModel(2, "Squat", listOf()),
                SessionExerciseModel(3, "Deadlift", listOf())
            )
        )  // ✅ Explicit return
    }
)