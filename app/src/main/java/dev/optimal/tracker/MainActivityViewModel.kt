package dev.optimal.tracker

import androidx.lifecycle.ViewModel
import dev.optimal.tracker.data.repository.WorkoutSessionRepository
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val workoutSessionRepository: WorkoutSessionRepository
) : ViewModel() {

}
