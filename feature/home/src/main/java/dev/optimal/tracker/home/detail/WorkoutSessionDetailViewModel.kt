package dev.optimal.tracker.home.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.optimal.tracker.data.repository.WorkoutSessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutSessionDetailViewModel @Inject constructor(
    private val workoutSessionRepository: WorkoutSessionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sessionId: Long = checkNotNull(savedStateHandle["sessionId"])

    private val _uiState = MutableStateFlow<WorkoutSessionDetailState>(WorkoutSessionDetailState.Loading)
    val uiState: StateFlow<WorkoutSessionDetailState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val session = workoutSessionRepository.getWorkoutSessionById(sessionId)
            if (session != null) {
                _uiState.value = WorkoutSessionDetailState.Success(session)
            } else {
                _uiState.value = WorkoutSessionDetailState.Error("Session not found")
            }
        }
    }
}
