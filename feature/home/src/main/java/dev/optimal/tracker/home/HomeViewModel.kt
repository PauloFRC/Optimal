package dev.optimal.tracker.home

import android.os.Build
import androidx.annotation.RequiresApi
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

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val workoutSessionRepository: WorkoutSessionRepository
) : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val _uiState = MutableStateFlow(HomeState())
    @RequiresApi(Build.VERSION_CODES.O)
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    init {
//        observeWorkoutSessions()
    }

    fun handle(action: HomeAction) {
        when(action) {
            else -> Unit
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
