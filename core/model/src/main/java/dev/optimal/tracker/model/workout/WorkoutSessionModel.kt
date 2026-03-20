package dev.optimal.tracker.model.workout

import java.time.LocalDateTime

data class WorkoutSessionModel(
    val id: Long,
    val workoutModelId: Long?,
    val name: String,
    val isCompleted: Boolean,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime?,
    val exercises: List<SessionExerciseModel>
)

//TODO: change to look at db for PRs for each exercise
fun WorkoutSessionModel.getPersonalRecords() : List<SessionExerciseModel> {
    return exercises.filter { it.getBestSet()?.weight != null }
}
