package dev.optimal.tracker.workout

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.optimal.tracker.data.repository.WorkoutSessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutSessionRepository: WorkoutSessionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(WorkoutState())
    val uiState: StateFlow<WorkoutState> = _uiState.asStateFlow()

    init {
        //observeWorkoutTemplates() TODO
    }

    fun handle(action: WorkoutAction) {
        when(action) {
            else -> Unit
        }
    }
}