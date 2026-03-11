package dev.optimal.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.optimal.tracker.designsystem.theme.OptimalTheme
import dev.optimal.tracker.ui.OptimalApp

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            OptimalTheme {
                OptimalApp(
                    state = uiState,
                    onAction = {}
                )
            }
        }
    }
}
