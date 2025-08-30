package dev.optimal.tracker.ui.screens.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class OptimalUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val workoutProgress: Int = 0,
    val errorMessage: String? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
//    private val userRepository: UserRepository,
//    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OptimalUiState())
    val uiState: StateFlow<OptimalUiState> = _uiState.asStateFlow()

    /**
     * Load user data when the app starts
     */
//    fun loadUserData() {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true) }
//
//            try {
//                val userData = userRepository.getUserData()
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        userName = userData.name,
//                        workoutProgress = userData.progressPercentage
//                    )
//                }
//            } catch (e: Exception) {
//                _uiState.update {
//                    it.copy(
//                        isLoading = false,
//                        errorMessage = "Failed to load user data: ${e.message}"
//                    )
//                }
//            }
//        }
//    }
}