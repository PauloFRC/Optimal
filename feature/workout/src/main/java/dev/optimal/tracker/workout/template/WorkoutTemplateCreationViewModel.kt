package dev.optimal.tracker.workout.template

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.optimal.tracker.data.repository.WorkoutTemplateRepository
import dev.optimal.tracker.model.workout.enums.SetType
import dev.optimal.tracker.model.workout.input.TemplateExerciseInput
import dev.optimal.tracker.model.workout.input.TemplateSetInput
import dev.optimal.tracker.model.workout.input.WorkoutTemplateInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutTemplateCreationViewModel @Inject constructor(
    private val workoutTemplateRepository: WorkoutTemplateRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(WorkoutTemplateCreationState())
    val uiState: StateFlow<WorkoutTemplateCreationState> = _uiState.asStateFlow()

    fun onWorkoutTemplateUpdate(update: (WorkoutTemplateInput) -> WorkoutTemplateInput) {
        _uiState.update { it.copy(workoutTemplate = update(it.workoutTemplate)) }
    }

    fun addExercise(exerciseId: Long) {
        _uiState.update { state ->
            val currentExercises = state.workoutTemplate.exercises
            val newExercise = TemplateExerciseInput(
                exerciseId = exerciseId,
                order = currentExercises.size + 1,
                sets = listOf(TemplateSetInput(order = 1, type = SetType.WORKING)) //TODO: implement
            )
            state.copy(
                workoutTemplate = state.workoutTemplate.copy(
                    exercises = currentExercises + newExercise
                )
            )
        }
    }

    fun saveWorkoutTemplate() {
        viewModelScope.launch {
            workoutTemplateRepository.addWorkoutTemplate(_uiState.value.workoutTemplate)
        }
    }
}
