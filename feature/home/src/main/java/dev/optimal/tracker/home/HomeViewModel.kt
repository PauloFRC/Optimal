package dev.optimal.tracker.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.optimal.tracker.data.repository.WorkoutSessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val workoutSessionRepository: WorkoutSessionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        observeWorkoutSessions()
    }

    fun handle(action: HomeAction) {
        when(action) {
            else -> Unit
        }
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
