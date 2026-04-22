package dev.optimal.tracker.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.optimal.tracker.core.model.IconAction
import dev.optimal.tracker.core.ui.components.OptimalTopAppBar
import dev.optimal.tracker.core.ui.components.SearchTopAppBar
import dev.optimal.tracker.core.utils.debounced
import dev.optimal.tracker.core.utils.debouncedClicks
import dev.optimal.tracker.designsystem.theme.OptimalTheme
import dev.optimal.tracker.feature.home.R
import dev.optimal.tracker.home.components.SessionHistoryCard
import dev.optimal.tracker.model.workout.WorkoutSessionModel
import dev.optimal.tracker.navigation.transition.NAV_ANIM_DURATION_MS
import dev.optimal.tracker.utils.OptimalDateTimeFormatter
import dev.optimal.tracker.core.designsystem.R as CoreR

@Composable
fun HomeScreenRoute(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onSessionClick: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    BackHandler(enabled = isSearchActive) {
        isSearchActive = false
        searchQuery = ""
    }

    val filteredSessions = remember(uiState.sessionHistory, searchQuery) {
        if (searchQuery.isBlank()) {
            uiState.sessionHistory
        } else {
            uiState.sessionHistory.filter { session ->
                session.name.startsWith(prefix = searchQuery, ignoreCase = true)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (isSearchActive) {
            SearchTopAppBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onCloseClicked = {
                    isSearchActive = false
                    searchQuery = ""
                }
            )
        } else {
            OptimalTopAppBar(
                title = stringResource(R.string.feature_home_title),
                actions = listOf(
                    IconAction(
                        titleRes = CoreR.string.core_designsystem_search,
                        iconRes = CoreR.drawable.ic_search,
                        onClick = { isSearchActive = true }
                    )
                )
            )
        }

        HomeScreen(
            sessions = filteredSessions,
            onSessionClick = onSessionClick,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            isSearchActive = isSearchActive,
            onClearSearchClick = {
                searchQuery = ""
            }
        )
    }
}

@Composable
fun HomeScreen(
    sessions: List<WorkoutSessionModel>,
    onSessionClick: (Long) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    isSearchActive: Boolean = false,
    onClearSearchClick: () -> Unit = {}
) {
    if (sessions.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.feature_home_empty_title),
                style = MaterialTheme.typography.bodyLarge
            )

            if (isSearchActive) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onClearSearchClick) {
                    Text(text = stringResource(R.string.feature_home_clear_search))
                }
            }
        }
        return
    }
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        itemsIndexed(
            items = sessions,
            key = { _, session -> session.id }
        ) { index, session ->
            MonthIndicator(
                currentSession = session,
                previousSession = sessions.getOrNull(index - 1),
                sessions = sessions
            )
            with(sharedTransitionScope) {
                SessionHistoryCard(
                    session = session,
                    onClick = { onSessionClick(session.id) }.debounced(),
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "session_bounds_${session.id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                        )
                )
            }
        }
    }
}

@Composable
fun MonthIndicator(
    currentSession: WorkoutSessionModel,
    previousSession: WorkoutSessionModel?,
    sessions: List<WorkoutSessionModel>
) {
    val isNewMonth = previousSession == null ||
            currentSession.startDate.month != previousSession.startDate.month ||
            currentSession.startDate.year != previousSession.startDate.year

    if (isNewMonth) {
        val sessionCount = sessions.count {
            it.startDate.month == currentSession.startDate.month &&
                    it.startDate.year == currentSession.startDate.year
        }
        val countText = pluralStringResource(
            id = R.plurals.feature_home_session_count,
            count = sessionCount,
            sessionCount
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = OptimalDateTimeFormatter
                    .formatMonth(currentSession.startDate)
                    .uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.1.em)
            )
            Text(
                text = countText,
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.1.em)
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    OptimalTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                HomeScreen(
                    sessions = HomeState().sessionHistory,
                    onSessionClick = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenEmptyPreview() {
    OptimalTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                HomeScreen(
                    sessions = listOf(),
                    onSessionClick = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this
                )
            }
        }
    }
}
