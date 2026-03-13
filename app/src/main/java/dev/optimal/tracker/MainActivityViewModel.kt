package dev.optimal.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.optimal.tracker.data.repository.WorkoutSessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val workoutSessionRepository: WorkoutSessionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    init {
        observeWorkoutSessions()
    }

    private fun observeWorkoutSessions() {
        workoutSessionRepository
            .getOrderedWorkoutSessions()
            .onEach { sessions ->
                _uiState.update { state ->
                    state.copy(sessionHistory = sessions)
                }
            }
            .launchIn(viewModelScope)
    }
}