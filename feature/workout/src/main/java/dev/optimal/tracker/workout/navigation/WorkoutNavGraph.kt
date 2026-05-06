package dev.optimal.tracker.workout.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.optimal.tracker.navigation.DetailRoute
import dev.optimal.tracker.navigation.TopLevelRoute
import dev.optimal.tracker.workout.WorkoutScreenRoute
import dev.optimal.tracker.workout.exercise.ExerciseSelectionScreen
import dev.optimal.tracker.workout.template.WorkoutTemplateCreationScreenRoute

fun NavGraphBuilder.workoutNavGraph(
    onNavigateToCreateTemplate: () -> Unit,
    onNavigateToDetail: () -> Unit,
    onNavigateToExerciseSelection: () -> Unit,
    onSelectExercise: (Long) -> Unit,
    onNavigateBack: () -> Unit,
) {
    composable<TopLevelRoute.Workout> {
        WorkoutScreenRoute(
            onNavigateToCreateTemplate = onNavigateToCreateTemplate,
            onNavigateToDetail = onNavigateToDetail,
            onNavigateBack = onNavigateBack
        )
    }

    composable<DetailRoute.WorkoutTemplateCreation> {
        WorkoutTemplateCreationScreenRoute(
            onBackClick = onNavigateBack,
            onAddExerciseClick = onNavigateToExerciseSelection,
            savedStateHandle = it.savedStateHandle
        )
    }

    composable<DetailRoute.ExerciseSelection> {
        ExerciseSelectionScreen(
            onExerciseSelected = onSelectExercise,
            onBackClick = onNavigateBack
        )
    }
}
